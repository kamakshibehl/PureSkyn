package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PriceDetailsDTO {
    private double halfPrice;
    private double fullPrice;

    public PriceDetailsDTO(double halfPrice, double fullPrice) {
        this.halfPrice = halfPrice;
        this.fullPrice = fullPrice;
    }

    public double getHalfPrice() {
        return halfPrice;
    }

    public void setHalfPrice(double halfPrice) {
        this.halfPrice = halfPrice;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(double fullPrice) {
        this.fullPrice = fullPrice;
    }
}
