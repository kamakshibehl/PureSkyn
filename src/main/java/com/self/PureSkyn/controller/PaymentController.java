package com.self.PureSkyn.controller;

import com.razorpay.Order;
import com.self.PureSkyn.Model.ApiResponse;
import com.self.PureSkyn.Model.ApiResponseStatus;
import com.self.PureSkyn.Model.Payment;
import com.self.PureSkyn.Model.response.BookingDTO;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/booking")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createPayment(@RequestBody double amount) {
        try {
            Map<String, Object> response = paymentService.createOrder(amount);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Payment requested successfully", response));
        } catch (BadRequestException e) {
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

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> payload) {
        return paymentService.verifyPayment(payload);
    }

}
