package com.project.ecom.dto;

import com.project.ecom.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDto {
    public String email;
    public Role role;
    public String token;
}
