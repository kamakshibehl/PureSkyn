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
}
