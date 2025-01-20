package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
public class Session {
    @Id
    private int id;
    private String day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int duration;
}
