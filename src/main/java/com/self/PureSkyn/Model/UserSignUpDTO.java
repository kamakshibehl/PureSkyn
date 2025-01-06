package com.self.PureSkyn.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserSignUpDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String role;
}
