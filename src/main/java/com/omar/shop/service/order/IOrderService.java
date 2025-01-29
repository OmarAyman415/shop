package com.omar.shop.service.order;

import com.omar.shop.dto.OrderDto;
import com.omar.shop.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
