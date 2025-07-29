package online.store.services.shedulers;

import org.springframework.transaction.annotation.Transactional;

public interface Scheduler {

    @Transactional
    void changePrice();
}
