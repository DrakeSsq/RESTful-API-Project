package online.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.entity.Order;
import online.store.entity.OrderItem;
import online.store.entity.Product;
import online.store.entity.enums.Status;
import online.store.exception.CustomerNotFoundException;
import online.store.exception.InsufficientProductsException;
import online.store.exception.ProductNotFoundException;
import online.store.repository.CustomerRepository;
import online.store.repository.OrderRepository;
import online.store.repository.ProductRepository;
import online.store.request.OrderRequest;
import online.store.request.ProductInfo;
import online.store.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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



        orderRequest.getProducts().forEach(item -> {
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

        orderRepository.save(order);

        return order.getId();
    }
}
