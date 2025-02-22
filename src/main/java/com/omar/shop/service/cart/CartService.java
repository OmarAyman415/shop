package com.omar.shop.service.cart;

import com.omar.shop.dto.CartDto;
import com.omar.shop.dto.CartItemDto;
import com.omar.shop.exceptions.ResourceNotFoundException;
import com.omar.shop.model.Cart;
import com.omar.shop.model.CartItem;
import com.omar.shop.model.User;
import com.omar.shop.repository.CartItemRepository;
import com.omar.shop.repository.CartRepository;
import com.omar.shop.service.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);

        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long cardId) {
        Cart cart = getCart(cardId);

        cartItemRepository.deleteAllByCartId(cardId);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.deleteById(cardId);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);

        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(cartRepository.findByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }


    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public CartDto convertToCartDto(Cart cart) {
        List<CartItemDto> cartItemDtos = convertProductToProductDto(cart.getItems());
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        cartDto.setItems(new HashSet<>(cartItemDtos));
        return cartDto;
    }

    private List<CartItemDto> convertProductToProductDto(Set<CartItem> cartItems) {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItems.forEach(cartItem -> {
            //map all data to cartItemDto by matching the field's names
            CartItemDto cartItemDto = modelMapper.map(cartItem, CartItemDto.class);
            cartItemDto.setProduct(productService.convertToDto(cartItem.getProduct()));

            cartItemDtos.add(cartItemDto);
        });

        return cartItemDtos;
    }
}
