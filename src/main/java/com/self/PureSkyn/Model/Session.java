package com.self.PureSkyn.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "sessions")
public class Session {
    @Id
    private String id;
    private LocalDate date;
    private LocalDateTime time;
}
