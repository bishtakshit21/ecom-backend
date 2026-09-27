package com.project.ecom.service;

import com.project.ecom.dto.AuthRequestDto;
import com.project.ecom.dto.AuthResponseDto;
import com.project.ecom.dto.UserRequestDto;
import com.project.ecom.dto.UserResponseDto;
import com.project.ecom.model.User;
import com.project.ecom.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {
    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;
    private UserRepo userRepo;
    private UserService userService;


    public AuthService(AuthenticationManager authenticationManager,JwtEncoder jwtEncoder,UserRepo userRepo,UserService userService){
     this.authenticationManager=authenticationManager;
     this.jwtEncoder=jwtEncoder;
     this.userRepo=userRepo;
     this.userService=userService;

    }

    public AuthResponseDto login(AuthRequestDto authRequestDto){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestDto.getEmail(),
                authRequestDto.getPassword())
        );

       User user =userRepo.findByEmail(authentication.getName()).
               orElseThrow(()->new UsernameNotFoundException("not available"));

     AuthResponseDto authResponseDto=new AuthResponseDto();
     authResponseDto.setEmail(user.getEmail());
     authResponseDto.setRole(user.getRole());
     authResponseDto.setToken(generateToken(user.getEmail(), user.getRole().toString()));
     return authResponseDto;
    }

    public AuthResponseDto register(UserRequestDto userRequestDto){
        UserResponseDto userResponseDto=userService.CreateUser(userRequestDto);
        AuthResponseDto authResponseDto=new AuthResponseDto();
        authResponseDto.setEmail(userResponseDto.getEmail());
        authResponseDto.setRole(userResponseDto.getRole());
        authResponseDto.setToken(generateToken(authResponseDto.getEmail(), authResponseDto.getRole().toString()));
        return authResponseDto;
    }
    private String generateToken(String email , String role){
        Instant now = Instant.now();
        String scope = role;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(email)
                .claim("scope", role)
                .build();
        JwsHeader jwsHeader=JwsHeader.with(MacAlgorithm.HS256).build();

        JwtEncoderParameters parameters=JwtEncoderParameters.from(jwsHeader,claims);
        return jwtEncoder.encode(parameters).getTokenValue();
    }
}
