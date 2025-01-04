package com.self.PureSkyn.Model;

import lombok.Data;

@Data
public class Session {
    private int id;
    private String day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int duration;
}
