package com.sahilkadian.ecommerce.controllers;

import com.sahilkadian.ecommerce.dto.AuthResponseDto;
import com.sahilkadian.ecommerce.dto.MessageDto;
import com.sahilkadian.ecommerce.dto.UpdatePasswordDto;
import com.sahilkadian.ecommerce.services.AuthService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestParam String email, @RequestParam String password){
        return new ResponseEntity<>(authService.loginUser(email,password), HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<MessageDto> logoutUser(){
        return new ResponseEntity<>(authService.logoutUser(),HttpStatus.OK);
    }

    @PostMapping("forgot")
    public ResponseEntity<MessageDto> forgotPassword(@RequestParam String email){
        return new ResponseEntity<>(authService.forgotPassword(email),HttpStatus.OK);
    }

    @PatchMapping("reset")
    public ResponseEntity<MessageDto> forgotPassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto, @RequestParam String token){
        return new ResponseEntity<>(authService.resetPassword(updatePasswordDto,token),HttpStatus.OK);
    }
}
