package online.store.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.controller.OrderController;
import online.store.request.OrderRequest;
import online.store.response.ResponseDto;
import online.store.service.OrderService;
import org.springframework.web.bind.annotation.RestController;

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
}
