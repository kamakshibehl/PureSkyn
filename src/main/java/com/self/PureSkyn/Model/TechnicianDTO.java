package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Data
@Getter
public class TechnicianDTO {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
