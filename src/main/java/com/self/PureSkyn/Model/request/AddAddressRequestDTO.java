package com.self.PureSkyn.Model.request;

import com.self.PureSkyn.Model.Address;

public class AddAddressRequestDTO {
    private String userId;
    private Address address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
