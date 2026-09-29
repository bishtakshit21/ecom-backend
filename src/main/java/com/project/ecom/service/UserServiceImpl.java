package com.project.ecom.service;

import com.project.ecom.Exception.ResourceNotFoundException;
import com.project.ecom.dto.UserRequestDto;
import com.project.ecom.dto.UserResponseDto;
import com.project.ecom.enums.Role;
import com.project.ecom.model.User;
import com.project.ecom.repository.UserRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepo userRepo , PasswordEncoder passwordEncoder){
        this.userRepo=userRepo;
        this.passwordEncoder=passwordEncoder;
    }
    @Override
    @Caching(put ={ @CachePut(value = "users", key = "#result.id")},
             evict={ @CacheEvict(value = "users", key = "'all'")})

    public UserResponseDto CreateUser(UserRequestDto userRequestDto) {
       User user =new User();
       user.setName(userRequestDto.getName());
       user.setAddress(userRequestDto.getAddress());
       user.setEmail(userRequestDto.getEmail());
       user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
       user.setRole(Role.ROLE_USER);
       User saved =userRepo.save(user);
        return maptorepsonse(saved);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponseDto GetById(Long id) {
        User user =userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("the record with is not present woth the id : "+id));
        return maptorepsonse(user);
    }

    @Override
    @Cacheable(value = "users", key = "'all'")
    public List<UserResponseDto> GetAll() {
        return userRepo.findAll().stream()
                                 .map(this::maptorepsonse)
                                 .toList();
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void DeleteById(Long id) {
        if(!userRepo.existsById(id)){
            throw new ResourceNotFoundException("the use with this id is not present : " + id);
        }
        userRepo.deleteById(id);
    }

    @Override
    @Cacheable(value = "users", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
    public UserResponseDto findMe() {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        return maptorepsonse(userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("the user does not exist")));
    }

    UserResponseDto maptorepsonse(User user){
       return new UserResponseDto(
               user.getId(),
               user.getName(),
               user.getEmail(),
               user.getAddress(),
               user.getRole()
       );
    }
}
