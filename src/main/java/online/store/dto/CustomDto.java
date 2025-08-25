package online.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomDto {
    UUID productId;
    UUID orderId;
    Long customerId;
    String status;
    String deliveryAddress;
    Integer quantity;
    String login;
    String email;
}
