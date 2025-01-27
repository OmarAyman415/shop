package com.omar.shop.controller;

import com.omar.shop.dto.CartDto;
import com.omar.shop.exceptions.ResourceNotFoundException;
import com.omar.shop.model.Cart;
import com.omar.shop.response.ApiResponse;
import com.omar.shop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @GetMapping("/{cardId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cardId) {
        try {
            Cart cart = cartService.getCart(cardId);
            CartDto cartDto = cartService.convertToCartDto(cart);
            return ResponseEntity.ok(new ApiResponse("Cart retrieved successfully", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cardId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cardId) {
        try {
            cartService.clearCart(cardId);
            return ResponseEntity.ok(new ApiResponse("Cart Deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{cardId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long cardId) {
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(cardId);
            return ResponseEntity.ok(new ApiResponse("Total price retrieved successfully", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
