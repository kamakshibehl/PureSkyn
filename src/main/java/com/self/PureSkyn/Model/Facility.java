package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@Data
@Document(collection = "services")
public class Facility {
    @Id
    private String serviceId;
    private String treatmentName;
    private String packageName;

    private String packagePrice;
    private String strikeOutPrice;

    private String featureName;
    private String selectedPackageImg;
    private String duration;

    private String category;
    private String packageType;
    private List<FacilityTypes> types;

}
