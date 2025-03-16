package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.ApiResponse;
import com.self.PureSkyn.Model.ApiResponseStatus;
import com.self.PureSkyn.Model.Cart;
import com.self.PureSkyn.Model.CartRequestDTO;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/add-update")
    public ResponseEntity<ApiResponse<Cart>> addToCart(@RequestBody CartRequestDTO cartRequest) {
        try {
            Cart cartItem = cartService.addToCart(
                    cartRequest.getUserId(),
                    cartRequest.getPackageDetails()
            );
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Item added to cart", cartItem));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "Failed to add item to cart"));
        }
    }

//    @PatchMapping("/update")
//    public ResponseEntity<ApiResponse<Cart>> updateCartItemQuantity(@RequestBody CartRequestDTO cartRequest) {
//        try {
//            Cart updatedItem = cartService.updateCartQuantity(
//                    cartRequest.getUserId(),
//                    cartRequest.getServiceId(),
//                    cartRequest.getSubServiceId(),
//                    cartRequest.getQuantity()
//            );
//            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Cart item updated", updatedItem));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, "Cart item not found"));
//        }
//    }


    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Cart>>> getCarts(@PathVariable String userId) {
        try {
            List<Cart> cart = cartService.getCarts(userId);

            if (cart.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.FAIL, "No cart items found for user ID: " + userId, null));
            }
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Cart items fetched successfully.", cart));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }

//    @DeleteMapping("/remove/{id}")
//    public ResponseEntity<ApiResponse<String>> removeCart(@PathVariable String id) {
//        try {
//            cartService.removeCart(id);
//            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Cart item removed"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, "Cart item not found"));
//        }
//    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable String userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Cart cleared successfully."));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }
    
}
