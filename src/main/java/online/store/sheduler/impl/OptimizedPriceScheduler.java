package online.store.sheduler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Product;
import online.store.interfaces.CalculateTransactionTime;
import online.store.report.ReportGenerator;
import online.store.repostitory.ProductRepository;
import online.store.sheduler.service.SchedulerProcessorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static online.store.util.LogMessageUtil.*;

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

    @Value("${spring.scheduling.batch.size}")
    private int BATCH_SIZE;

    @CalculateTransactionTime
    @Scheduled(fixedDelayString = "${spring.scheduling.fixed.delay}")
    @Transactional
    public void runOptimizedUpdate() {

        reportGenerator.clearData();

        int page = 0;
        Page<Product> batch;

        do {
            batch = productRepository.findProductsOptimistic(PageRequest.of(page, BATCH_SIZE));
            if (!batch.isEmpty()) {
                try {
                    schedulerProcessorService.processBatch(batch);
                } catch (Exception e) {
                    log.error(ERROR_UPDATE_PRICE, e.getMessage());
                }
                page++;
            }
        } while (!batch.isEmpty());

        log.info(SUCCESSFUL_UPDATE);
    }
}
