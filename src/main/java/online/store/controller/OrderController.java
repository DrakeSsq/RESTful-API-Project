package online.store.controller;

import jakarta.validation.Valid;
import online.store.request.OrderRequest;
import online.store.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/order")
public interface OrderController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseDto<UUID> createOrder(@RequestHeader("customerId") Long id,
                                  @RequestBody @Valid OrderRequest orderRequest);

}
