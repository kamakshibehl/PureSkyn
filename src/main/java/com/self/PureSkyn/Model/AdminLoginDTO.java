package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AdminLoginDTO {
    private String id;
    private String email;
    private String name;
    private String phone;
    private String role;
    private String token;

    public AdminLoginDTO(String id, String email, String name, String phone, String role, String token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
