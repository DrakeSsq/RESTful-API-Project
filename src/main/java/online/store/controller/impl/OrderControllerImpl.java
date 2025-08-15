package online.store.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.controller.OrderController;
import online.store.request.OrderRequest;
import online.store.request.ProductInfo;
import online.store.request.StatusRequest;
import online.store.response.ResponseDto;
import online.store.response.ResponseOrder;
import online.store.service.OrderService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static online.store.util.LogMessageUtil.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    @Override
    public ResponseDto<UUID> createOrder(Long id, OrderRequest orderRequest) {
        return ResponseDto.<UUID>builder()
                .data(orderService.createOrder(id, orderRequest))
                .message(SUCCESS_CREATE)
                .build();
    }

    @Override
    public ResponseDto<UUID> addOrderItem(UUID orderId, Long customerId, List<ProductInfo> productInfoList) {
        return ResponseDto.<UUID>builder()
                .data(orderService.addOrderItem(orderId, customerId, productInfoList))
                .message(ADD_ITEM_TO_ORDER)
                .build();
    }

    @Override
    public ResponseOrder getOrder(UUID orderId, Long customerId) {
        return orderService.getOrder(orderId, customerId);
    }

    @Override
    public ResponseDto<Void> deleteOrder(UUID orderId, Long customerId) {
        orderService.deleteOrder(orderId, customerId);
        return ResponseDto.<Void>builder()
                .message(ORDER_HAS_BEEN_DELETED)
                .build();
    }

    @Override
    public ResponseDto<UUID> confirmOrder(UUID orderId, Long customerId) {
        return ResponseDto.<UUID>builder()
                .data(orderService.confirmOrder(orderId, customerId))
                .message(ORDER_HAS_BEEN_CONFIRMED)
                .build();
    }

    @Override
    public ResponseDto<Void> changeStatus(UUID orderId, Long customerId, StatusRequest status) {
        orderService.changeStatus(orderId, customerId, status);
        return ResponseDto.<Void>builder()
                .message(STATUS_CHANGED)
                .build();
    }
}

