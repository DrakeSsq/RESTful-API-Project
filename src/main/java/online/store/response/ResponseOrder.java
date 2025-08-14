package online.store.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOrder {
    private UUID orderId;
    private List<OrderProductDto> products = new ArrayList<>();
    private BigDecimal totalPrice;
}
