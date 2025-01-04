package com.self.PureSkyn.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "admins")
public class Admin {
    @Id
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role = Role.ADMIN;
}
