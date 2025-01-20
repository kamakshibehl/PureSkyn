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

}
