package com.omar.shop.service.cart;

import com.omar.shop.dto.CartDto;
import com.omar.shop.model.Cart;
import com.omar.shop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    CartDto convertToCartDto(Cart cart);

    Cart getCartByUserId(Long userId);
}
