package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Cart;
import com.self.PureSkyn.Model.CartPackageDetailsDTO;
import com.self.PureSkyn.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepo;

    public Cart addToCart(String userId, List<CartPackageDetailsDTO> packageDetails) {
        Optional<Cart> existingCart = cartRepo.findByUserId(userId);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setPackageDetails(packageDetails);
            return cartRepo.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setPackageDetails(packageDetails);
            return cartRepo.save(newCart);
        }
    }

    public List<Cart> getCarts(String userId) {
        return cartRepo.findByUserId(userId)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }


//    public Cart updateCartQuantity(String userId, String serviceId, String subServiceId, int quantity) {
//        Cart Cart = cartRepo.findByUserIdAndServiceIdAndSubServiceId(userId, serviceId, subServiceId)
//                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
//
//        Cart.setQuantity(quantity);
//        return cartRepo.save(Cart);
//    }

//    public void removeCart(String id) {
//        cartRepo.deleteById(id);
//    }

    public void clearCart(String userId) {
        cartRepo.deleteByUserId(userId);
    }
}
