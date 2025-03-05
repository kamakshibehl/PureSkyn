package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@Document(collection = "cart")
public class Cart {
    @Id
    private String id;
    private String userId;
    private String serviceId;
    private String subServiceId;
    private int quantity;

    public Cart(String userId, String serviceId, String subServiceId, int quantity) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.subServiceId = subServiceId;
        this.quantity = quantity;
    }

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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSubServiceId() {
        return subServiceId;
    }

    public void setSubServiceId(String subServiceId) {
        this.subServiceId = subServiceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
