package com.sahilkadian.ecommerce.controllers;

import com.sahilkadian.ecommerce.dto.*;
import com.sahilkadian.ecommerce.services.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @PostMapping("register")
    public ResponseEntity<MessageDto> register(@Valid @RequestBody SellerRegisterDto sellerRegisterDto){
        return new ResponseEntity<>(sellerService.register(sellerRegisterDto),HttpStatus.OK);
    }

    @GetMapping("view-profile")
    public ResponseEntity<SellerResponseDto> viewProfile(){
        return new ResponseEntity<>(sellerService.viewProfile(),HttpStatus.OK);
    }

    @PatchMapping("update-profile")
    public ResponseEntity<MessageDto> updateProfile(@RequestBody SellerDto sellerDto){
        return new ResponseEntity<>(sellerService.updateProfile(sellerDto),HttpStatus.OK);
    }

    @PatchMapping("update-password")
    public ResponseEntity<MessageDto> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto){
        return new ResponseEntity<>(sellerService.updatePassword(updatePasswordDto),HttpStatus.OK);
    }

    @PatchMapping("update-address")
    public ResponseEntity<MessageDto> updateAddress(@RequestParam Long id, @RequestBody AddressDto addressDto){
        return new ResponseEntity<>(sellerService.updateAddress(id,addressDto), HttpStatus.OK);
    }
}
