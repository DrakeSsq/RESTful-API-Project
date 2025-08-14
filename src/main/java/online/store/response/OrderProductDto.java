package online.store.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderProductDto {
    private UUID productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
