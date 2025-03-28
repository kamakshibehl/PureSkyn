package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@Data
public class FacilityTypes {
    private String subServiceId;
    private String treatmentName;
    private String packageName;
    private String packagePrice;
    private String strikeOutPrice;
    private String featureName;
    private String selectedPackageImg;
    private String duration;
    private int noOfSessions;
}
