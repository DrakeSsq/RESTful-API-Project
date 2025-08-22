package online.store.dto;

import lombok.*;
import online.store.entity.enums.Status;

import java.util.UUID;

@Builder
@Getter
public class OrderInfo {
    private UUID id;
    private CustomerInfo customer;
    private Status status;
    private String deliveryAddress;
    private Integer quantity;
}
