package online.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.store.entity.enums.Status;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    UUID id;
    CustomerInfo customer;
    Status status;
    String deliveryAddress;
    Integer quantity;
}
