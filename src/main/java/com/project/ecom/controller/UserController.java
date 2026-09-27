package com.project.ecom.controller;

import com.project.ecom.dto.UserRequestDto;
import com.project.ecom.dto.UserResponseDto;
import com.project.ecom.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
private UserService userService;
public UserController(UserService userService){
    this.userService=userService;
}
@PreAuthorize("hasAnyRole('ADMIN')")
@GetMapping("/getall")
    public ResponseEntity<List<UserResponseDto>> getallusers(){
    return ResponseEntity.ok(userService.GetAll());
}
@PreAuthorize("hasAnyRole('ADMIN')")
@GetMapping("/getbyid/{id}")
    public ResponseEntity<UserResponseDto> getuserbyid(@PathVariable Long id){
return ResponseEntity.ok(userService.GetById(id));
}
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> findMe(){
    return ResponseEntity.ok(userService.findMe());
    }
    @PreAuthorize("hasAnyRole('ADMIN')")

@PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto){
    return new ResponseEntity<>(userService.CreateUser(userRequestDto), HttpStatus.CREATED);
}
    @PreAuthorize("hasAnyRole('ADMIN')")

@DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> DeleteUser(@PathVariable Long id){
    userService.DeleteById(id);
    return ResponseEntity.ok("deleted sucessfully");
}

}
