package com.project.ecom.dto;

import com.project.ecom.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long Id;
    private String Name;
    private String email;
    private String Address;
    private Role Role;
}
