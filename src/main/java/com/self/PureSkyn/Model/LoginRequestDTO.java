package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
