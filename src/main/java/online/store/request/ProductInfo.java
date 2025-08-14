package online.store.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductInfo {
    private UUID id;
    private Integer quantity;
}
