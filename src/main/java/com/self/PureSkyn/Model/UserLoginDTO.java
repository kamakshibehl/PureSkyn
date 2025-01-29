package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
public class UserLoginDTO {
    @Id
    private String Id;
    private String email;
    private String name;
    private String phone;
    private String token;

    public UserLoginDTO(String id, String email, String name, String phone, String token) {
        this.Id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.token = token;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
}
