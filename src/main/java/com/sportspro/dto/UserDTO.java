package com.sportspro.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Lombok will generate getters, setters, toString(), equals, and hashcode
@NoArgsConstructor  // Default no-argument constructor
@AllArgsConstructor  // Constructor with arguments for all fields
public class UserDTO {

    private Long userId;

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    private String email;

    private String bio;
    private String location;
    private String sport;
    private String avatar;

}
