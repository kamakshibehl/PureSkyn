package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
public class UserSignUpDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
}
