package online.store.sheduler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Product;
import online.store.report.ReportGenerator;
import online.store.report.dto.ReportDataDto;
import online.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static online.store.util.LogMessageUtil.ERROR_IN_PROCESS_METHOD;
import static online.store.util.LogMessageUtil.ERROR_WRITING_CSV;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerProcessorService {

    private final ProductRepository productRepository;
    private final ReportGenerator reportGenerator;

    @Value("${price.update.multiplier:1.1}")
    private BigDecimal multiplier;

    @Transactional
    public void processBatch(Page<Product> batch) {
        try {
            batch.forEach(product -> {
                BigDecimal oldPrice = product.getPrice();
                product.setPrice(oldPrice.multiply(multiplier));

                try {
                    addReportData(product.getId(), oldPrice, product.getPrice());
                } catch (IOException e) {
                    log.error(ERROR_WRITING_CSV, e.getMessage());
                }

            });
            productRepository.saveAll(batch);
        } catch (Exception e) {
            log.error(ERROR_IN_PROCESS_METHOD, e.getMessage());
        }
    }

    private void addReportData(UUID id, BigDecimal oldPrice, BigDecimal newPrice) throws IOException {
        reportGenerator.writeData(ReportDataDto.builder()
                        .id(id)
                        .oldPrice(oldPrice)
                        .newPrice(newPrice)
                        .build());
    }

}
