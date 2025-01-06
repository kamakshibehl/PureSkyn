package com.self.PureSkyn.Model;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String token;

    public UserLoginDTO(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
