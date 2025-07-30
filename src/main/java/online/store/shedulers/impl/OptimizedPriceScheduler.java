package online.store.shedulers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Product;
import online.store.interfaces.Timeable;
import online.store.report.ReportGenerator;
import online.store.repostitories.ProductRepository;
import online.store.shedulers.services.SchedulerProcessorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static online.store.util.LogMessageUtil.SUCCESSFUL_ERRORS_COUNTER;

@Service
@Profile({"prod", "local"})
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "spring.scheduling.optimized.enabled",
        havingValue = "true")
public class OptimizedPriceScheduler {

    private final ProductRepository productRepository;
    private final SchedulerProcessorService schedulerProcessorService;
    private final ReportGenerator reportGenerator;

    @Value("${batch.size}")
    private int BATCH_SIZE;

    @Timeable
    @Scheduled(fixedDelayString = "${fixed.delay}")
    public void runOptimizedUpdate() {

        reportGenerator.clearData();

        int page = 0;
        List<Product> batch;
        int successCount = 0;
        int errorCount = 0;

        do {
            batch = productRepository.findBatchWithOptimisticLock(PageRequest.of(page, BATCH_SIZE));
            if (!batch.isEmpty()) {
                if (schedulerProcessorService.processBatch(batch, BATCH_SIZE)) {
                    successCount += batch.size();
                } else {
                    errorCount += batch.size();
                }
                page++;
            }
        } while (!batch.isEmpty());

        log.info(SUCCESSFUL_ERRORS_COUNTER, successCount, errorCount);
    }
}
