package online.store.services.shedulers.impl;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.interfaces.Timeable;
import online.store.repostitories.ProductRepository;
import online.store.services.shedulers.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static online.store.util.LogMessageUtil.UPDATING_CONFLICT_OPTIMIZED;
import static online.store.util.LogMessageUtil.UPDATING_PRICES_OPTIMIZED;

@Service
@Profile("prod")
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "spring.scheduling.optimized.enabled",
        havingValue = "true")
public class OptimizedPriceSchedulerImpl implements Scheduler {

    private final ProductRepository productRepository;

    @Timeable
    @Scheduled(fixedDelayString = "${fixed.delay}")
    public void changePrice() {

        try {
            productRepository.updateAllPricesOptimistic(BigDecimal.valueOf(1.1));
            log.info(UPDATING_PRICES_OPTIMIZED);

        } catch (OptimisticLockException e) {
            log.info(UPDATING_CONFLICT_OPTIMIZED);
        }
    }
}
