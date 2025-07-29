package online.store.services.shedulers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Product;
import online.store.interfaces.Timeable;
import online.store.report.ReportGenerator;
import online.store.repostitories.ProductRepository;
import online.store.services.shedulers.services.SchedulerProcessorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Profile("prod")
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "spring.scheduling.optimized.enabled",
        havingValue = "true")
public class OptimizedPriceScheduler {

    private final ProductRepository productRepository;
    private final ReportGenerator reportGenerator;
    private final SchedulerProcessorService schedulerProcessorService;

    private static final int BATCH_SIZE = 100;

    @Timeable
    @Scheduled(fixedDelayString = "${fixed.delay}")
    public void runOptimizedUpdate() {
        int page = 0;
        List<Product> batch;
        int successCount = 0;
        int errorCount = 0;

        do {
            batch = productRepository.findBatch(page * BATCH_SIZE, BATCH_SIZE);
            if (!batch.isEmpty()) {
                if (schedulerProcessorService.processBatch(batch)) {
                    successCount += batch.size();
                } else {
                    errorCount += batch.size();
                }
                page++;
            }
        } while (!batch.isEmpty());

        reportGenerator.generateReport();
        log.info("Успешно: {}, Ошибки: {}", successCount, errorCount);
    }
}
