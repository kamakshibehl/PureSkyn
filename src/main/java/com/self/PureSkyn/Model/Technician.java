package com.self.PureSkyn.Model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "technicians")
public class Technician {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private List<String> serviceTypes;
    private List<String> availablePinCodes;

    // private List<TimeSlot> timeSlots;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<String> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public List<String> getAvailablePinCodes() {
        return availablePinCodes;
    }

    public void setAvailablePinCodes(List<String> availablePinCodes) {
        this.availablePinCodes = availablePinCodes;
    }

    public String getName() {
        return firstName + " " + lastName;
    }
}
