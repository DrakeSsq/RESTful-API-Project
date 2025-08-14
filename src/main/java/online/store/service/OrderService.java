package online.store.service;

import online.store.request.OrderRequest;

import java.util.UUID;

public interface OrderService {

    UUID createOrder(Long id, OrderRequest orderRequest);
}
