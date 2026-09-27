package com.project.ecom.service;

import com.project.ecom.dto.UserRequestDto;
import com.project.ecom.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserResponseDto CreateUser(UserRequestDto userRequestDto);
    UserResponseDto GetById(Long id);
    List<UserResponseDto> GetAll();
    void DeleteById(Long id);

    UserResponseDto findMe();
}
