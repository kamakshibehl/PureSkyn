package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartPackageDetailsDTO {
    private String treatmentName;
    private String packageName;
    private String packagePrice;
    private String strikeOutPrice;
    private String serviceId;
    private String subServiceId;
    private String featureName;
    private String selectedPackageImg;
}
