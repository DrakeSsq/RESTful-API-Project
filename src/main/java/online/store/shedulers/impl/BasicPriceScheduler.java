package online.store.services.shedulers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Product;
import online.store.interfaces.Timeable;
import online.store.report.ReportGenerator;
import online.store.report.dto.ReportDataDto;
import online.store.repostitories.ProductRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    @Timeable
    @Scheduled(fixedDelayString = "${fixed.delay}")
    public void changePrice() {

        List<Product> productList = productRepository.findAllWithPessimisticLock();

        productList.forEach(product -> {
            BigDecimal oldPrice = product.getPrice();
            BigDecimal newPrice = oldPrice.multiply(BigDecimal.valueOf(1.1));

            product.setPrice(newPrice);

            reportGenerator.addData(ReportDataDto.builder()
                    .id(product.getId())
                    .oldPrice(oldPrice)
                    .newPrice(newPrice)
                    .build());
        });

        productRepository.saveAll(productList);
        reportGenerator.generateReport();

        log.info(UPDATING_PRICES_BASIC, productList.size());
    }
}
