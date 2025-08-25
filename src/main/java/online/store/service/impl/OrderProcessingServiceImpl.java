package online.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.dto.CustomDto;
import online.store.dto.CustomerInfo;
import online.store.dto.OrderInfo;
import online.store.entity.enums.Status;
import online.store.repository.OrderItemRepository;
import online.store.response.AccountResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.*;

import static online.store.util.LogMessageUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingServiceImpl {

    private final OrderItemRepository orderItemRepository;
    private final WebClient webClient;

    @Value("${microservice.account-service.path-to-report}")
    private String pathUri;

    public Map<UUID, List<OrderInfo>> getAggregatedProductsOrder() throws ExecutionException, InterruptedException {

        Map<UUID, List<OrderInfo>> result = new HashMap<>();
        Set<String> logins = new HashSet<>();

        List<CustomDto> data = orderItemRepository.getDataToReport();
        for (CustomDto dto : data) {
            Status status = Status.valueOf(dto.getStatus());
            logins.add(dto.getLogin());

            OrderInfo orderInfo = OrderInfo.builder()
                    .id(dto.getOrderId())
                    .customer(CustomerInfo.builder()
                            .id(dto.getCustomerId())
                            .login(dto.getLogin())
                            .accountNumber(null)
                            .email(dto.getEmail())
                            .inn(null)
                            .build())
                    .status(status)
                    .deliveryAddress(dto.getDeliveryAddress())
                    .quantity(dto.getQuantity())
                    .build();


            result.computeIfAbsent(dto.getProductId(), k -> new ArrayList<>())
                    .add(orderInfo);
        }

        CompletableFuture<AccountResponse> completableFuture = webClient.post()
                .uri(pathUri)
                .bodyValue(logins)
                .retrieve()
                .bodyToMono(AccountResponse.class)
                .toFuture()
                .exceptionally(ex -> {

                            if (ex.getCause() instanceof ConnectException) {
                                log.error(CONNECT_EXCEPTION, pathUri);
                                return null;
                            } else if (ex.getCause() instanceof TimeoutException) {
                                log.error(TIMEOUT_EXCEPTION, pathUri);
                                return null;
                            } else {
                                log.error(UNEXPECTED_EXCEPTION, pathUri, ex.getCause().getMessage(), ex.getCause());
                                return null;
                            }
                        });


        AccountResponse accountResponse = completableFuture.get();

        if (accountResponse == null) {
            log.warn(ACCOUNT_RESPONSE_IS_NULL);
            return result;
        }

        result.values().stream().flatMap(List::stream)
                .forEach(orderInfo -> {
                    String login = orderInfo.getCustomer().getLogin();
                    String accountNumber = accountResponse.getAccounts().get(login);
                    if (accountNumber != null) {
                        orderInfo.getCustomer().setAccountNumber(accountNumber);
                    }
                });

        return result;
    }
}
