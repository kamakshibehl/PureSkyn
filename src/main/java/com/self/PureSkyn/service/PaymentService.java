package com.self.PureSkyn.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.self.PureSkyn.Model.Payment;
import com.self.PureSkyn.repository.PaymentRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class PaymentService {
    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentRepo paymentRepo;

    @Value("${payment.gateway.keyId}")
    private String keyId;

    @Value("${payment.gateway.keySecret}")
    private String keySecret;


    public PaymentService() {
        try {
            razorpayClient = new RazorpayClient(keyId, keySecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a Razorpay order and returns the order details.
     *
     * @param amount the order amount in INR (multiplied by 100 to convert into paise)
     * @return JSON string containing the order details, including order ID
     */
    public Map<String, Object> createOrder(double amount) {
        try {

            String receiptId = UUID.randomUUID().toString();

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int)(amount * 100)); // in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receiptId);

            Order order = razorpayClient.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", (int)(amount * 100));
            response.put("currency", "INR");
            response.put("key", keyId);
            response.put("name", "PureSkyn");
            response.put("description", "Booking Payment");

            return response;
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
            return null;
        }
    }

    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> payload) {
        String razorpayPaymentId = payload.get("razorpay_payment_id");
        String razorpayOrderId = payload.get("razorpay_order_id");
        String razorpaySignature = payload.get("razorpay_signature");
        String razorpayAmount = payload.get("razorpay_amount");

        double amount = Double.parseDouble(razorpayAmount);


        try {
            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setTransactionId(razorpayPaymentId);
            payment.setPaymentDate(LocalDate.now());
            payment.setPaymentTime(LocalDateTime.now());

            String payloadToHash = razorpayOrderId + "|" + razorpayPaymentId;

            String generatedSignature = PaymentService.calculateRazorpaySignature(payloadToHash, keySecret);

            if (generatedSignature.equals(razorpaySignature)) {
                Payment payment1 = paymentRepo.save(payment);
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(payment1.getId());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("❌ Signature mismatch: payment invalid");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("⚠️ Error verifying payment: " + e.getMessage());
        }
    }

    public Optional<Payment> getPaymentDetail(String paymentId) {
        return paymentRepo.findById(paymentId);
    }

    public static String calculateRazorpaySignature(String data, String keySecret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return new String(Base64.getEncoder().encode(hash));
    }
}