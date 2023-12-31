package com.sahilkadian.ecommerce.services;

import com.sahilkadian.ecommerce.dto.*;
import com.sahilkadian.ecommerce.entities.Address;
import com.sahilkadian.ecommerce.entities.Role;
import com.sahilkadian.ecommerce.entities.Seller;
import com.sahilkadian.ecommerce.exceptions.AlreadyExistsException;
import com.sahilkadian.ecommerce.exceptions.PasswordMismatchException;
import com.sahilkadian.ecommerce.exceptions.UserNotFoundException;
import com.sahilkadian.ecommerce.repositories.*;
import com.sahilkadian.ecommerce.security.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private JwtTokenGenerator tokenGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Value("${role.seller}")
    private String SELLER;

    public MessageDto register(SellerRegisterDto sellerRegisterDto){

        if(userRepository.existsByEmail(sellerRegisterDto.getEmail())){
            throw new AlreadyExistsException("Username already taken!");
        }

        if(!sellerRegisterDto.getConfirmPassword().equals(sellerRegisterDto.getPassword())){
            throw new PasswordMismatchException("Confirm Password Doesn't Match");
        }

        Seller seller = new Seller();
        seller.setFirstName(sellerRegisterDto.getFirstName());
        seller.setMiddleName(sellerRegisterDto.getMiddleName());
        seller.setLastName(sellerRegisterDto.getLastName());
        seller.setEmail(sellerRegisterDto.getEmail());
        seller.setGst(sellerRegisterDto.getGst());
        seller.setCompanyName(sellerRegisterDto.getCompanyName());
        seller.setCompanyContact(sellerRegisterDto.getCompanyContact());
        seller.setPassword(passwordEncoder.encode(sellerRegisterDto.getPassword()));
        seller.setAddress(sellerRegisterDto.getAddress());
        seller.getAddress().setSeller(seller);
        seller.setCreatedBy(sellerRegisterDto.getEmail());
        seller.setUpdatedBy(sellerRegisterDto.getEmail());

        Role role = roleRepository.findByAuthority(SELLER).get();
        seller.setRoles(Collections.singleton(role));

        sellerRepository.save(seller);
        return new MessageDto("Seller registered successfully!");
    }

    public SellerResponseDto viewProfile(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(username).orElse(null);
        if(seller==null) {
            throw new UserNotFoundException("Seller Not Found");
        }
        SellerResponseDto sellerResponseDto = new SellerResponseDto();
        sellerResponseDto.setId(seller.getId());
        sellerResponseDto.setFirstName(seller.getFirstName());
        sellerResponseDto.setLastName(seller.getLastName());
        sellerResponseDto.setFullName(seller.getFirstName()+" "+seller.getLastName());
        sellerResponseDto.setEmail(seller.getEmail());
        sellerResponseDto.setIsActive(seller.isActive());
        sellerResponseDto.setCompanyName(seller.getCompanyName());
        sellerResponseDto.setCompanyContact(seller.getCompanyContact());
        sellerResponseDto.setCreated_by(seller.getCreatedBy());
        sellerResponseDto.setDate_created(seller.getDateCreated());
        sellerResponseDto.setLast_updated(seller.getLastUpdated());
        sellerResponseDto.setUpdated_by(seller.getUpdatedBy());

        Address address = new Address();
        address.setId(seller.getAddress().getId());
        address.setCity(seller.getAddress().getCity());
        address.setState(seller.getAddress().getState());
        address.setCountry(seller.getAddress().getCountry());
        address.setAddress_line(seller.getAddress().getAddress_line());
        address.setZip_code(seller.getAddress().getZip_code());
        address.setLabel(seller.getAddress().getLabel());

        sellerResponseDto.setCompanyAddress(address);
        return sellerResponseDto;
    }

    public MessageDto updateProfile(SellerDto sellerDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        Seller seller = sellerRepository.findByEmail(username).orElse(null);
        if(seller==null){
            throw new UserNotFoundException("Seller Not Found");
        }
        if(sellerDto.getFirstName()!=null) seller.setFirstName(sellerDto.getFirstName());
        if(sellerDto.getMiddleName()!=null) seller.setMiddleName(sellerDto.getMiddleName());
        if(sellerDto.getLastName()!=null) seller.setLastName(sellerDto.getLastName());
        if(sellerDto.getCompanyName()!=null) seller.setCompanyName(sellerDto.getCompanyName());
        if(sellerDto.getCompanyContact()!=null) seller.setCompanyContact(sellerDto.getCompanyContact());
        if(sellerDto.getGst()!=null) seller.setGst(sellerDto.getGst());
        seller.setUpdatedBy(username);
        sellerRepository.save(seller);
        return new MessageDto("Seller Updated Successfully");
    }

    public MessageDto updatePassword(UpdatePasswordDto updatePasswordDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(username).orElse(null);
        if(seller==null){
            throw new UserNotFoundException("Seller Not Found");
        }
        if(!updatePasswordDto.getPassword().equals(updatePasswordDto.getConfirmPassword())){
            throw new PasswordMismatchException("Confirm Password Doesn't Match");
        }
        seller.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
        seller.setUpdatedBy(username);
        sellerRepository.save(seller);
        emailSenderService.sendEmail(username,"Your Password Has Been Changed Successfully.","Password Changed");
        return new MessageDto("Seller Password Updated Successfully");
    }

    public MessageDto updateAddress(Long id, AddressDto addressDto) {
        Address address = addressRepository.findById(id).orElse(null);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(username).orElse(null);
        if(address==null || seller==null || !seller.getId().equals(address.getSeller().getId())){
            throw new UserNotFoundException("Address Id Not Found");
        }
        if(addressDto.getCity()!=null) address.setCity(addressDto.getCity());
        if(addressDto.getState()!=null) address.setState(addressDto.getState());
        if(addressDto.getCountry()!=null) address.setCountry(addressDto.getCountry());
        if(addressDto.getAddress_line()!=null) address.setAddress_line(addressDto.getAddress_line());
        if(addressDto.getZip_code()!=null) address.setZip_code(addressDto.getZip_code());
        if(addressDto.getLabel()!=null) address.setLabel(addressDto.getLabel());
        addressRepository.save(address);

        seller.setUpdatedBy(username);
        sellerRepository.save(seller);
        return new MessageDto("Address Updated Successfully.");
    }
}
