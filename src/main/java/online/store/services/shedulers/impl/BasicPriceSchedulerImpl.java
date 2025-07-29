package online.store.services.shedulers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Product;
import online.store.interfaces.Timeable;
import online.store.repostitories.ProductRepository;
import online.store.services.shedulers.Scheduler;
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
public class BasicPriceSchedulerImpl implements Scheduler {

    private final ProductRepository productRepository;

    @Timeable
    @Scheduled(fixedDelayString = "${fixed.delay}")
    public void changePrice() {

        List<Product> productList = productRepository.findAllWithPessimisticLock();

        List<Product> updatedProducts = productList.stream()
                .peek(p ->
                        p.setPrice(
                                p.getPrice().multiply(BigDecimal.valueOf(1.1)))).toList();

        productRepository.saveAll(updatedProducts);

        log.info(UPDATING_PRICES_BASIC, updatedProducts.size());
    }
}
