package online.store.shedulers.services;

import lombok.RequiredArgsConstructor;
import online.store.entity.Product;
import online.store.interfaces.Timeable;
import online.store.report.ReportGenerator;
import online.store.report.dto.ReportDataDto;
import online.store.repostitories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    public boolean processBatch(List<Product> batch, int batchSize) {
        try {
            batch.forEach(product -> {
                BigDecimal oldPrice = product.getPrice();
                product.setPrice(oldPrice.multiply(BigDecimal.valueOf(1.1)));

                try {
                    addReportData(product.getId(), oldPrice, product.getPrice());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            productRepository.saveAll(batch);
            return true;
        } catch (Exception e) {
            return false;
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
