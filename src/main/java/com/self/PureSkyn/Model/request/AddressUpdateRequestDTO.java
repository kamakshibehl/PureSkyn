package com.self.PureSkyn.Model.request;

import com.self.PureSkyn.Model.Address;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AddressUpdateRequestDTO {
    private String userId;
    private String addressId;
    private Address updatedAddress;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Address getUpdatedAddress() {
        return updatedAddress;
    }

    public void setUpdatedAddress(Address updatedAddress) {
        this.updatedAddress = updatedAddress;
    }
}
