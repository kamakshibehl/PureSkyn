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
    }
}
