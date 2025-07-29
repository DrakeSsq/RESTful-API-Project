package online.store.services.shedulers.services;

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

@Service
@RequiredArgsConstructor
public class SchedulerProcessorService {

    private final ProductRepository productRepository;
    private final ReportGenerator reportGenerator;
    private final PlatformTransactionManager txManager;

    @Timeable
    @Transactional
    public boolean processBatch(List<Product> batch) {
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            batch.forEach(product -> {
                BigDecimal oldPrice = product.getPrice();
                product.setPrice(oldPrice.multiply(BigDecimal.valueOf(1.1)));
                reportGenerator.addData(ReportDataDto.builder()
                        .id(product.getId())
                        .oldPrice(oldPrice)
                        .newPrice(product.getPrice())
                        .build());
            });
            productRepository.saveAll(batch);
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            txManager.rollback(status);
            return false;
        }
    }

}
