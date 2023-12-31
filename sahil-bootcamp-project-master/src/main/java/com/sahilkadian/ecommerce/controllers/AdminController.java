package com.sahilkadian.ecommerce.controllers;

import com.sahilkadian.ecommerce.dto.CustomerResponseDto;
import com.sahilkadian.ecommerce.dto.MessageDto;
import com.sahilkadian.ecommerce.dto.SellerResponseDto;
import com.sahilkadian.ecommerce.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("customers")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers
            (@RequestParam(required = false,defaultValue = "10") int pageSize,
             @RequestParam(required = false,defaultValue = "0") int pageOffset,
             @RequestParam(required = false,defaultValue = "id") String sortedBy,
             @RequestParam(required = false,defaultValue = "ASC") String sortDirection){
        return new ResponseEntity<>(adminService.getAllCustomers(pageSize,pageOffset,sortedBy,sortDirection), HttpStatus.OK);
    }

    @GetMapping("sellers")
    public ResponseEntity<List<SellerResponseDto>> getAllSellers
            (@RequestParam(required = false,defaultValue = "10") int pageSize,
             @RequestParam(required = false,defaultValue = "0") int pageOffset,
             @RequestParam(required = false,defaultValue = "id") String sortedBy,
             @RequestParam(required = false,defaultValue = "ASC") String sortDirection){
        return new ResponseEntity<>(adminService.getAllSellers(pageSize,pageOffset,sortedBy,sortDirection),HttpStatus.OK);
    }

    @PatchMapping("activate-deactivate")
    public ResponseEntity<MessageDto> activateUser(@RequestParam Long id, @RequestParam Boolean active){
        return new ResponseEntity<>(adminService.activateDeactivateUser(id,active),HttpStatus.OK);
    }

    @PatchMapping("lock-unlock")
    public ResponseEntity<MessageDto> lockUnlockUser(@RequestParam Long id,@RequestParam Boolean lock){
        return new ResponseEntity<>(adminService.lockUnlockUser(id,lock),HttpStatus.OK);
    }
}
