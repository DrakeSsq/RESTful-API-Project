package online.store.shedulers.services;

import lombok.RequiredArgsConstructor;
import online.store.entity.Product;
import online.store.interfaces.Timeable;
import online.store.report.ReportGenerator;
import online.store.report.dto.ReportDataDto;
import online.store.repostitories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchedulerProcessorService {

    private final ProductRepository productRepository;
    private final ReportGenerator reportGenerator;

    @Timeable
    @Transactional
    public boolean processBatch(List<Product> batch) {
        try {
            batch.forEach(product -> {
                BigDecimal oldPrice = product.getPrice();
                product.setPrice(oldPrice.multiply(BigDecimal.valueOf(1.1)));
                addReportData(product.getId(), oldPrice, product.getPrice());
            });
            productRepository.saveAll(batch);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void addReportData(UUID id, BigDecimal oldPrice, BigDecimal newPrice) {
        reportGenerator.addData(ReportDataDto.builder()
                        .id(id)
                        .oldPrice(oldPrice)
                        .newPrice(newPrice)
                        .build());
    }

}
