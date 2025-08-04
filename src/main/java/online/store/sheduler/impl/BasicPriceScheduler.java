package online.store.sheduler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Product;
import online.store.interfaces.CalculateTransactionTime;
import online.store.report.ReportGenerator;
import online.store.report.dto.ReportDataDto;
import online.store.repostitory.ProductRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

import static online.store.util.LogMessageUtil.ERROR_WRITING_CSV;
import static online.store.util.LogMessageUtil.UPDATING_PRICES_BASIC;

@Service
@Profile("local")
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.scheduling.basic.enabled",
                        havingValue = "true")
public class BasicPriceScheduler {

    private final ProductRepository productRepository;
    private final ReportGenerator reportGenerator;

    @Value("${price.update.multiplier:1.1}")
    private BigDecimal multiplier;

    @Value("${spring.scheduling.batch.size}")
    private Integer BATCH_SIZE;

    @CalculateTransactionTime
    @Scheduled(fixedDelayString = "${spring.scheduling.fixed.delay}")
    @Transactional
    public void changePrice() {

        int page = 0;

        reportGenerator.clearData();

        Page<Product> productList;

        do {

            productList = productRepository.findAll(PageRequest.of(page, BATCH_SIZE));

            productList.forEach(product -> {
                BigDecimal oldPrice = product.getPrice();
                BigDecimal newPrice = oldPrice.multiply(multiplier);

                product.setPrice(newPrice);

                try {
                    reportGenerator.writeData(ReportDataDto.builder()
                            .id(product.getId())
                            .oldPrice(oldPrice)
                            .newPrice(newPrice)
                            .build());
                } catch (IOException e) {
                    log.error(ERROR_WRITING_CSV, e.getMessage());
                }
            });

            productRepository.saveAll(productList);

            page++;

        } while (!productList.isEmpty());

        log.info(UPDATING_PRICES_BASIC, productList.stream().count());
    }
}
