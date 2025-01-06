package com.self.PureSkyn.Model;

import lombok.Data;

@Data
public class Facility {
    private int id;
    private String name;
    private String description;
    private String type;
    private double price;
    //private String[] validPinCodes;
}
