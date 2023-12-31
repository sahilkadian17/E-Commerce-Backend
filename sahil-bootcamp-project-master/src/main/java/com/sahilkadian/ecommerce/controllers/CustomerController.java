package com.sahilkadian.ecommerce.controllers;

import com.sahilkadian.ecommerce.dto.*;
import com.sahilkadian.ecommerce.entities.Address;
import com.sahilkadian.ecommerce.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("register")
    public ResponseEntity<MessageDto> registerCustomer(@Valid @RequestBody CustomerRegisterDto customerRegisterDto){
        return new ResponseEntity<>(customerService.register(customerRegisterDto),HttpStatus.OK);
    }

    @PutMapping("activate")
    public ResponseEntity<MessageDto> activateCustomer(@RequestParam String token){
        return new ResponseEntity<>(customerService.activate(token),HttpStatus.OK);
    }

    @PostMapping("resend")
    public ResponseEntity<MessageDto> resendActivationLink(@RequestParam String email){
        return new ResponseEntity<>(customerService.resendActivationLink(email),HttpStatus.OK);
    }

    @GetMapping("view-profile")
    public ResponseEntity<CustomerResponseDto> viewProfile(){
        return new ResponseEntity<>(customerService.viewProfile(),HttpStatus.OK);
    }

    @PatchMapping("update-profile")
    public ResponseEntity<MessageDto> updateProfile(@RequestBody CustomerDto customerDto){
        return new ResponseEntity<>(customerService.updateProfile(customerDto),HttpStatus.OK);
    }

    @PatchMapping("update-password")
    public ResponseEntity<MessageDto> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto){
        return new ResponseEntity<>(customerService.updatePassword(updatePasswordDto),HttpStatus.OK);
    }

    @GetMapping("view-addresses")
    public ResponseEntity<List<Address>> viewAddresses(){
        return new ResponseEntity<>(customerService.viewAddresses(), HttpStatus.OK);
    }

    @PostMapping("add-address")
    public ResponseEntity<MessageDto> addAddress(@RequestBody AddressDto addressDto){
        return new ResponseEntity<>(customerService.addAddress(addressDto), HttpStatus.OK);
    }

    @DeleteMapping("delete-address")
    public ResponseEntity<MessageDto> deleteAddress(@RequestParam Long id){
        return new ResponseEntity<>(customerService.deleteAddress(id), HttpStatus.OK);
    }

    @PatchMapping("update-address")
    public ResponseEntity<MessageDto> updateAddress(@RequestParam Long id, @RequestBody AddressDto addressDto){
        return new ResponseEntity<>(customerService.updateAddress(id,addressDto),HttpStatus.OK);
    }
}
