package online.store.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderRequest {

    private String deliveryAddress;

    private List<ProductInfo> products;
}
