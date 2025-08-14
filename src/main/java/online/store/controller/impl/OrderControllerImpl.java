package online.store.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.controller.OrderController;
import online.store.request.OrderRequest;
import online.store.request.ProductInfo;
import online.store.response.ResponseDto;
import online.store.response.ResponseOrder;
import online.store.service.OrderService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    @Override
    public ResponseDto<UUID> createOrder(Long id, OrderRequest orderRequest) {
        return ResponseDto.<UUID>builder()
                .data(orderService.createOrder(id, orderRequest))
                .message("Заказ создан")
                .build();
    }

    @Override
    public ResponseDto<UUID> addOrderItem(UUID orderId, Long customerId, List<ProductInfo> productInfoList) {
        return ResponseDto.<UUID>builder()
                .data(orderService.addOrderItem(orderId, customerId, productInfoList))
                .message("Заказ создан")
                .build();
    }

    @Override
    public ResponseOrder getOrder(UUID orderId, Long customerId) {
        return orderService.getOrder(orderId, customerId);
    }
}

