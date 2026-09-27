package com.project.ecom.controller;

import com.project.ecom.dto.AuthRequestDto;
import com.project.ecom.dto.AuthResponseDto;
import com.project.ecom.dto.UserRequestDto;
import com.project.ecom.dto.UserResponseDto;
import com.project.ecom.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto){
        return  ResponseEntity.ok(authService.login(authRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(authService.register(userRequestDto));
    }
}
