package online.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.dto.CustomDto;
import online.store.dto.CustomerInfo;
import online.store.dto.OrderInfo;
import online.store.entity.enums.Status;
import online.store.repository.OrderItemRepository;
import online.store.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingServiceImpl {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final WebClient webClient;

    public Map<UUID, List<OrderInfo>> getAggregatedProductsOrder() {

        Map<UUID, List<OrderInfo>> result = new HashMap<>();
        Set<String> logins = new HashSet<>();

        List<CustomDto> data = orderItemRepository.getDataToReport();
        for (CustomDto dto : data) {
            Status status = Status.valueOf(dto.getStatus());
            logins.add(dto.getLogin());

            OrderInfo orderInfo = new OrderInfo(
                    dto.getOrderId(),
                    new CustomerInfo(dto.getCustomerId(), dto.getLogin(), null, dto.getEmail(), null),
                    status,
                    dto.getDeliveryAddress(),
                    dto.getQuantity()
            );

            result.computeIfAbsent(dto.getProductId(), k -> new ArrayList<>())
                    .add(orderInfo);
        }

        return result;


//        Set<String> logins = orders.stream()
//                .map(orderInfo -> orderInfo.getCustomer().getLogin())
//                .collect(Collectors.toSet());
//
//
//        CompletableFuture<AccountResponse> completable = webClient.post()
//                            .uri("/mock/accounts/find-by-logins")
//                            .bodyValue(logins)
//                            .retrieve()
//                            .bodyToMono(AccountResponse.class)
//                            .toFuture();
//
//        AccountResponse loginToAccountMap = completable.join();
//
//        loginToAccountMap.getAccounts()
    }
}
