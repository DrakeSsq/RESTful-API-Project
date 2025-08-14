package online.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Customer;
import online.store.entity.Order;
import online.store.entity.OrderItem;
import online.store.entity.Product;
import online.store.entity.enums.Status;
import online.store.exception.*;
import online.store.repository.CustomerRepository;
import online.store.repository.OrderRepository;
import online.store.repository.ProductRepository;
import online.store.request.OrderRequest;
import online.store.request.ProductInfo;
import online.store.response.OrderProductDto;
import online.store.response.ResponseOrder;
import online.store.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static online.store.util.LogMessageUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;


    @Override
    @Transactional
    public UUID createOrder(Long id, OrderRequest orderRequest) {

        Order order = new Order();

        order.setCustomer(customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND, id))));
        order.setStatus(Status.CREATED);
        order.setDeliveryAddress(orderRequest.getDeliveryAddress());

        processProductInfo(order, orderRequest.getProducts());

        orderRepository.save(order);

        return order.getId();
    }

    @Override
    @Transactional
    public UUID addOrderItem(UUID orderId, Long customerId, List<ProductInfo> productInfoList) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND, customerId)));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));

        if (order.getCustomer() != customer) {
            throw new EditSomeoneElseOrderException(String.format(SOMEONE_ELSE_ORDER, customerId, orderId));
        }

        processProductInfo(order, productInfoList);

        orderRepository.save(order);

        return order.getId();
    }

    @Override
    public ResponseOrder getOrder(UUID orderId, Long customerId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new ReceivingSomeoneElseOrderException(String.format(SOMEONE_ELSE_ORDER, customerId, orderId));
        }

        ResponseOrder response = new ResponseOrder();

        BigDecimal totalPrice = BigDecimal.ZERO;

        List<OrderProductDto> products = new ArrayList<>();

        for (OrderItem item : order.getItems()) {

            products.add(OrderProductDto.builder()
                    .productId(item.getProduct().getId())
                    .name(item.getProduct().getName())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build());

            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        response.setProducts(products);
        response.setTotalPrice(totalPrice);

        return response;

    }

    private void processProductInfo(Order order, List<ProductInfo> productInfoList) {
        productInfoList.forEach(item -> {
            OrderItem orderItem = new OrderItem();
            Product product = productRepository.findById(item.getId())
                    .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

            if (item.getQuantity() > product.getQuantity()) {
                throw new InsufficientProductsException(
                        String.format(NOT_ENOUGH_QUANTITY, product.getQuantity(), item.getQuantity()));
            }

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());

            product.setQuantity(product.getQuantity() - item.getQuantity());
            product.setLastQuantityUpdatedAt(LocalDateTime.now());

            order.getItems().add(orderItem);
        });
    }
}
