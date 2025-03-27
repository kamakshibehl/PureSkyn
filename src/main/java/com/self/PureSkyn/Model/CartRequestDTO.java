package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CartRequestDTO {
    private String userId;
    private List<CartPackageDetailsDTO> packageDetails;

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
