package com.sahilkadian.ecommerce.services;

import com.sahilkadian.ecommerce.dto.AuthResponseDto;
import com.sahilkadian.ecommerce.dto.MessageDto;
import com.sahilkadian.ecommerce.dto.UpdatePasswordDto;
import com.sahilkadian.ecommerce.entities.TokenStore;
import com.sahilkadian.ecommerce.entities.User;
import com.sahilkadian.ecommerce.exceptions.AlreadyExistsException;
import com.sahilkadian.ecommerce.exceptions.PasswordMismatchException;
import com.sahilkadian.ecommerce.exceptions.TokenExpiredException;
import com.sahilkadian.ecommerce.exceptions.UserNotFoundException;
import com.sahilkadian.ecommerce.repositories.TokenRepository;
import com.sahilkadian.ecommerce.repositories.UserRepository;
import com.sahilkadian.ecommerce.security.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private JwtTokenGenerator tokenGenerator;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.reset.expiration}")
    private long resetExpiry;

    public AuthResponseDto loginUser(String email,String password){
        User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenStore tokenStore = new TokenStore();
        if(tokenRepository.existsByUserId(user.getId())){
            return new AuthResponseDto(tokenRepository.findByEmail(email).get().getToken());
        }
        String token = tokenGenerator.generateToken(authentication);
        tokenStore.setToken(token);
        tokenStore.setDateCreated(new Date());
        tokenStore.setEmail(email);
        tokenStore.setUser(user);
        tokenRepository.save(tokenStore);
        return new AuthResponseDto(token);
    }

    public MessageDto logoutUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userRepository.existsByEmail(username)){
            throw new UserNotFoundException("User Not Found");
        }
        tokenRepository.deleteByEmail(username);
        return new MessageDto("User Logout");
    }

    public MessageDto forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user==null){
            throw new UserNotFoundException("User not found");
        }
        if(!user.isActive()){
            throw new AlreadyExistsException("User Account is Not Activated!");
        }
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(new Date(new Date().getTime()+resetExpiry));
        userRepository.save(user);
        emailSenderService.sendEmail(email,token,"Reset Password Token");
        return new MessageDto("Reset Password Token Send Successfully...");
    }

    public MessageDto resetPassword(UpdatePasswordDto updatePasswordDto,String token) {
        if(!updatePasswordDto.getPassword().equals(updatePasswordDto.getConfirmPassword())){
            throw new PasswordMismatchException("Confirm Password Doesn't Match");
        }
        User user = userRepository.findByResetPasswordToken(token).orElse(null);
        if(user==null){
            throw new UserNotFoundException("User Not Found");
        }
        if(user.getResetPasswordTokenExpiry().getTime()<System.currentTimeMillis()){
            throw new TokenExpiredException("Token Expired");
        }
        user.setResetPasswordToken(null);
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
        userRepository.save(user);
        return new MessageDto("Password Reset Successfully...");
    }
}
