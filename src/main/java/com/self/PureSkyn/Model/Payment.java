package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "payment")
public class Payment {
    @Id
    private String id;
    private double amount;
    private String transactionId;
    private String paymentMode;
    private LocalDate paymentDate;
    private LocalDateTime paymentTime;
}
