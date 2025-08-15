package online.store.request;

import lombok.Getter;
import online.store.entity.enums.Status;

@Getter
public class StatusRequest {
    private Status status;
}
