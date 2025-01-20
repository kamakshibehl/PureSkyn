package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class UserDetailsDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;
    List<Address> addresses;
    List<BookingDTO> bookings;
}
