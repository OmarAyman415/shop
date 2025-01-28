package com.omar.shop.service.order;

import com.omar.shop.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);

    Order getOrder(Long orderId);
}
