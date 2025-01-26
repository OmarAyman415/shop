package com.omar.shop.service.cart;

import com.omar.shop.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);

    void removeItemFromCart(Long cartId, Long productId);

    void updateItemQuantity(Long cartId, Long productId, int quantity);


    CartItem getCartItemById(Long cartId, Long productId);
}
