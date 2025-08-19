package online.store.controller;

import jakarta.validation.Valid;
import online.store.request.OrderRequest;
import online.store.request.ProductInfo;
import online.store.request.StatusRequest;
import online.store.response.ResponseDto;
import online.store.response.ResponseOrder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/order")
public interface OrderController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseDto<UUID> createOrder(@RequestHeader("customerId") Long id,
                                  @RequestBody @Valid OrderRequest orderRequest);

    @PatchMapping("/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseDto<UUID> addOrderItem(@PathVariable("orderId") UUID orderId,
                                   @RequestHeader("customerId") Long customerId,
                                   @RequestBody List<ProductInfo> productInfoList);

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseOrder getOrder(@PathVariable("orderId") UUID orderId,
                           @RequestHeader("customerId") Long customerId);

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseDto<Void> deleteOrder(@PathVariable("orderId") UUID orderId,
                                  @RequestHeader("customerId") Long customerId);

    @PostMapping("/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    ResponseDto<UUID> confirmOrder(@PathVariable("orderId") UUID orderId,
                                   @RequestHeader("customerId") Long customerId);

    @PatchMapping("/{orderId}/status")
    @ResponseStatus(HttpStatus.OK)
    ResponseDto<Void> changeStatus(@PathVariable("orderId") UUID orderId,
                                   @RequestHeader("customerId") Long customerId,
                                   @RequestBody StatusRequest statusRequest);

}
