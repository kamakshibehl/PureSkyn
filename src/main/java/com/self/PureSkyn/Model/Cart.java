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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartPackageDetailsDTO> getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(List<CartPackageDetailsDTO> packageDetails) {
        this.packageDetails = packageDetails;
    }
}
