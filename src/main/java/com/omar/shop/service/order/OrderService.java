package com.omar.shop.service.order;

import com.omar.shop.enums.OrderStatus;
import com.omar.shop.exceptions.ResourceNotFoundException;
import com.omar.shop.model.Cart;
import com.omar.shop.model.Order;
import com.omar.shop.model.OrderItem;
import com.omar.shop.model.Product;
import com.omar.shop.repository.OrderRepository;
import com.omar.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Order placeOrder(Long userId) {
        return null;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        // TODO: Add user id to order
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            updateProductInventory(product, cartItem.getQuantity());

            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private void updateProductInventory(Product product, int orderQuantity) {
        product.setInventory(product.getInventory() - orderQuantity);
        productRepository.save(product);
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> {
                    return orderItem.getPrice()
                            .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
}
