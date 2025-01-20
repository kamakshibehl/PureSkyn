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
    private String token;

    public UserLoginDTO(String id, String email, String token) {
        this.Id = id;
        this.email = email;
        this.token = token;
    }
}
