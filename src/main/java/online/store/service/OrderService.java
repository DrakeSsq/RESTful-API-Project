package online.store.service;

import online.store.entity.enums.Status;
import online.store.request.OrderRequest;
import online.store.request.ProductInfo;
import online.store.request.StatusRequest;
import online.store.response.ResponseOrder;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    UUID createOrder(Long id, OrderRequest orderRequest);

    UUID addOrderItem(UUID orderId, Long customerId, List<ProductInfo> productInfoList);

    ResponseOrder getOrder(UUID orderId, Long customerId);

    void deleteOrder(UUID orderId, Long customerId);

    UUID confirmOrder(UUID orderId, Long customerId);

    void changeStatus(UUID orderId, Long customerId, StatusRequest status);
}
