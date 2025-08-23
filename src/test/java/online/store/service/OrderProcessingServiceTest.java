package online.store.service;

import lombok.extern.slf4j.Slf4j;
import online.store.dto.CustomDto;
import online.store.dto.OrderInfo;
import online.store.repository.OrderItemRepository;
import online.store.service.impl.OrderProcessingServiceImpl;
import online.store.util.OrderProcessingServiceData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.wiremock.spring.EnableWireMock;

import java.util.*;
import java.util.concurrent.ExecutionException;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = OrderProcessingServiceTest.AppConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock
@Slf4j
@TestPropertySource(
        locations = "classpath:application-test.yaml")
public class OrderProcessingServiceTest {

    @Value("${wiremock.server.url}")
    private String wireMockUrl;

    @Value("${wiremock.server.port}")
    private int wiremockPort;

    @Autowired
    private OrderProcessingServiceImpl orderProcessingService;

    @MockBean
    private OrderItemRepository orderItemRepository;


    @Test
    void getAggregatedProductsOrderSuccess() throws ExecutionException, InterruptedException {

        List<CustomDto> mockData = OrderProcessingServiceData.getData();
        UUID orderId = mockData.getFirst().getOrderId();
        UUID productId = mockData.getFirst().getProductId();

        when(orderItemRepository.getDataToReport()).thenReturn(mockData);

        stubFor(post(urlEqualTo(wireMockUrl))
                .willReturn(okJson("{\"accounts\":{\"login1\":\"ACC12345\"}}")));

        Map<UUID, List<OrderInfo>> result = orderProcessingService.getAggregatedProductsOrder();



        assertThat(result).hasSize(1);
        assertThat(result.get(productId)).hasSize(1);

        OrderInfo orderInfo = result.get(productId).getFirst();
        assertThat(orderInfo).isNotNull();
        assertThat(orderInfo.getCustomer().getLogin()).isEqualTo(mockData.getFirst().getLogin());
        assertThat(orderInfo.getCustomer().getEmail()).isEqualTo(mockData.getFirst().getEmail());
        assertThat(orderInfo.getQuantity()).isEqualTo(mockData.getFirst().getQuantity());
        assertThat(orderInfo.getDeliveryAddress()).isEqualTo(mockData.getFirst().getDeliveryAddress());

    }

    @SpringBootApplication(scanBasePackages = "online.store")
    @EnableJpaRepositories(basePackages = "online.store.repository")
    @EntityScan(basePackages = "online.store.entity")
    static class AppConfiguration {}
}
