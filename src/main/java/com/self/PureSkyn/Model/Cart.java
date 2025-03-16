package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Getter
@Setter
@Document(collection = "cart")
public class Cart {
    @Id
    private String id;
    private String userId;
    private List<CartPackageDetailsDTO> packageDetails;
}
