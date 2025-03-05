package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Cart;
import com.self.PureSkyn.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepo;

    public Cart addToCart(String userId, String serviceId, String subServiceId, int quantity) {
        Cart Cart = cartRepo.findByUserIdAndServiceIdAndSubServiceId(userId, serviceId, subServiceId)
                .orElse(new Cart(userId, serviceId, subServiceId, 0));

        Cart.setQuantity(Cart.getQuantity() + quantity);
        return cartRepo.save(Cart);
    }

    public List<Cart> getCarts(String userId) {
        return cartRepo.findByUserId(userId);
    }

    public Cart updateCartQuantity(String userId, String serviceId, String subServiceId, int quantity) {
        Cart Cart = cartRepo.findByUserIdAndServiceIdAndSubServiceId(userId, serviceId, subServiceId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        Cart.setQuantity(quantity);
        return cartRepo.save(Cart);
    }

    public void removeCart(String id) {
        cartRepo.deleteById(id);
    }

    public void clearCart(String userId) {
        cartRepo.deleteByUserId(userId);
    }
}
