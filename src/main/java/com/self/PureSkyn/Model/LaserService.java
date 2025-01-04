package com.self.PureSkyn.Model;

import lombok.Data;

@Data
public class LaserService {
    private int id;
    private String name;
    private String description;
    private String type;
    private double price;
    private String[] validPinCodes;
}
