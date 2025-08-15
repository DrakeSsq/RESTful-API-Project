package online.store.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDto (
    UUID id,
    String productName,
    Integer quantity,
    BigDecimal price
) {}
