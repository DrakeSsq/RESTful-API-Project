package online.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.dto.OrderItemDto;
import online.store.entity.Order;
import online.store.entity.OrderItem;
import online.store.entity.Product;
import online.store.entity.enums.Status;
import online.store.exception.*;
import online.store.repository.CustomerRepository;
import online.store.repository.OrderItemRepository;
import online.store.repository.OrderRepository;
import online.store.repository.ProductRepository;
import online.store.request.OrderRequest;
import online.store.request.ProductInfo;
import online.store.request.StatusRequest;
import online.store.response.OrderProductDto;
import online.store.response.ResponseOrder;
import online.store.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static online.store.util.LogMessageUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;


    @Override
    @Transactional
    public UUID createOrder(Long id, OrderRequest orderRequest) {

        Order order = new Order();

        order.setCustomer(customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND, id))));
        order.setStatus(Status.CREATED);
        order.setDeliveryAddress(orderRequest.getDeliveryAddress());

        processProductInfo(order, orderRequest.getProducts()).forEach(orderItem -> {
            order.getItems().add(orderItem);
        });

        orderRepository.save(order);
        log.info(CREATE_ORDER, id, order.getId());

        return order.getId();
    }

    @Override
    @Transactional
    public UUID addOrderItem(UUID orderId, Long customerId, List<ProductInfo> productInfoList) {

        Order order = validCheck(orderId, customerId);

        processProductInfo(order, productInfoList);

        orderRepository.save(order);
        log.info(ADD_ORDER_ITEMS, orderId, productInfoList.size());

        return order.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseOrder getOrder(UUID orderId, Long customerId) {

        Order order = validCheck(orderId, customerId);

        List<OrderItemDto> items = orderItemRepository.findOrderItems(orderId);

        BigDecimal totalPrice = items.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info(GET_ORDER, orderId);

        return ResponseOrder.builder()
                .orderId(orderId)
                .products(items.stream()
                        .map(item -> new OrderProductDto(
                                item.id(),
                                item.productName(),
                                item.quantity(),
                                item.price()
                        ))
                        .toList())
                .totalPrice(totalPrice)
                .build();

    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId, Long customerId) {

        Order order = validCheck(orderId, customerId);

        if (!order.getStatus().equals(Status.CREATED)) {
            throw new ProhibitionOperationException(PROHIBITION_OPERATION);
        }

        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            product.setLastQuantityUpdatedAt(LocalDateTime.now());
        });

        order.setStatus(Status.CANCELLED);
        log.info(DELETE_ORDER, orderId);

    }

    @Override
    @Transactional
    public UUID confirmOrder(UUID orderId, Long customerId) {
        return null;
        //TODO
    }

    @Override
    @Transactional
    public void changeStatus(UUID orderId, Long customerId, StatusRequest status) {

        Order order = validCheck(orderId, customerId);

        switch (status.getStatus()) {
            case CANCELLED -> deleteOrder(orderId, customerId);
            default -> order.setStatus(status.getStatus());
        }

        log.info(CHANGE_ORDER_STATUS, orderId, status);
    }

    private Order validCheck(UUID orderId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));

        if (!order.getCustomer().getId().equals(customerId)) {
            log.warn(SOMEONE_ELSE_ORDER_LOG, customerId, orderId);
            throw new EditSomeoneElseOrderException(String.format(SOMEONE_ELSE_ORDER, customerId, orderId));
        }

        return order;
    }

    private List<OrderItem> processProductInfo(Order order, List<ProductInfo> productInfoList) {

        List<UUID> productsId = productInfoList.stream()
                .map(ProductInfo::getId).toList();

        Map<UUID, Product> products = productRepository.findAllById(productsId).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<OrderItem> orderItems = new ArrayList<>();

        productInfoList.forEach(item -> {
            Product product = products.get(item.getId());
            if (product == null) {
                throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
            }

            if (item.getQuantity() > product.getQuantity()) {
                throw new InsufficientProductsException(
                        String.format(NOT_ENOUGH_QUANTITY, product.getQuantity(), item.getQuantity()));
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());

            product.setQuantity(product.getQuantity() - item.getQuantity());
            product.setLastQuantityUpdatedAt(LocalDateTime.now());

            orderItems.add(orderItem);
        });

        return orderItems;
    }
}
