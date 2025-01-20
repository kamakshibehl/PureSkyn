package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Document(collection = "payment")
public class Payment {
    @Id
    private String id;
    private String bookingId;
    private boolean isFullPayment;
    private double amountPaid;
    private String transactionId;
    private String paymentMode;
    private LocalDateTime paymentDate;
}
