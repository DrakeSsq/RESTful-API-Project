package online.store.interfaces;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static online.store.util.LogMessageUtil.TIMEABLE_ASPECT_LOG;

/**
 * Аспект для замера времени выполнения транзакций.
 * Логирует время работы и результат (успешно/откат) для методов,
 * помеченных аннотацией @CalculateTransactionTime.
 */
@Aspect
@Component
@Slf4j
public class CalculateTransactionTimeAspect {

    @Around("@annotation(calculateTransactionTime)")
    public Object measureTransactionTime(ProceedingJoinPoint joinPoint, CalculateTransactionTime calculateTransactionTime) throws Throwable {

        long startTime = System.currentTimeMillis();

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionTimingSynchronization(startTime)
            );
        }

        return joinPoint.proceed();
    }

    private static class TransactionTimingSynchronization implements TransactionSynchronization {

        private final long startTime;

        private TransactionTimingSynchronization(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void afterCompletion(int status) {

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            String statusName = switch (status) {
                case STATUS_COMMITTED -> "COMMITTED";
                case STATUS_ROLLED_BACK -> "ROLLED_BACK";
                default -> "UNKNOWN";
            };

            log.info(TIMEABLE_ASPECT_LOG,
                    statusName,
                    duration);
        }
    }
}
