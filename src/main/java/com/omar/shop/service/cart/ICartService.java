package com.omar.shop.service.cart;

import com.omar.shop.dto.CartDto;
import com.omar.shop.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();

    CartDto convertToCartDto(Cart cart);

    Cart getCartByUserId(Long userId);
}
