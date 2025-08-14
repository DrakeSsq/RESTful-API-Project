package online.store.service;

import online.store.request.OrderRequest;
import online.store.request.ProductInfo;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    UUID createOrder(Long id, OrderRequest orderRequest);

    UUID addOrderItem(UUID orderId, Long customerId, List<ProductInfo> productInfoList);
}
