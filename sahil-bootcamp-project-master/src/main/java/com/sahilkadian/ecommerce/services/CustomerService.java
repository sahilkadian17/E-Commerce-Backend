package com.sahilkadian.ecommerce.services;

import com.sahilkadian.ecommerce.dto.*;
import com.sahilkadian.ecommerce.entities.*;
import com.sahilkadian.ecommerce.exceptions.AlreadyExistsException;
import com.sahilkadian.ecommerce.exceptions.PasswordMismatchException;
import com.sahilkadian.ecommerce.exceptions.TokenExpiredException;
import com.sahilkadian.ecommerce.exceptions.UserNotFoundException;
import com.sahilkadian.ecommerce.repositories.*;
import com.sahilkadian.ecommerce.security.CustomUserDetailsService;
import com.sahilkadian.ecommerce.security.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenGenerator tokenGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AddressRepository addressRepository;

    @Value("${jwt.reset.expiration}")
    private long resetExpiry;

    @Value("${role.customer}")
    private String CUSTOMER;

    public MessageDto register(CustomerRegisterDto customerRegisterDto){

        if(userRepository.existsByEmail(customerRegisterDto.getEmail())){
            throw new AlreadyExistsException("Username already taken");
        }

        if(!customerRegisterDto.getConfirmPassword().equals(customerRegisterDto.getPassword())){
            throw new PasswordMismatchException("Confirm password doesn't match");
        }

        Customer customer = new Customer();
        customer.setFirstName(customerRegisterDto.getFirstName());
        customer.setMiddleName(customerRegisterDto.getMiddleName());
        customer.setLastName(customerRegisterDto.getLastName());
        customer.setEmail(customerRegisterDto.getEmail());
        customer.setContact(customerRegisterDto.getContact());
        customer.setPassword(passwordEncoder.encode(customerRegisterDto.getPassword()));
        customer.setDateCreated(new Date());
        customer.setLastUpdated(new Date());
        customer.setCreatedBy(customerRegisterDto.getEmail());
        customer.setUpdatedBy(customerRegisterDto.getEmail());

        Role role = roleRepository.findByAuthority(CUSTOMER).orElse(null);
        customer.setRoles(Collections.singleton(role));

        String token = UUID.randomUUID().toString();
        customer.setActivationToken(token);
        customer.setActivationTokenExpiry(new Date(new Date().getTime()+resetExpiry));
        customerRepository.save(customer);

        String body = "http://localhost:8080/customer/activate?token="+token;
        emailSenderService.sendEmail(customerRegisterDto.getEmail(),body,"Activate your account");
        return new MessageDto("Customer registered successfully.");
    }

    public MessageDto activate(String token){
        User user = userRepository.findByActivationToken(token).orElse(null);
        if(user==null){
            throw new UserNotFoundException("User Not Found");
        }
        if(user.getActivationTokenExpiry().getTime()<System.currentTimeMillis()){
            throw new TokenExpiredException("Token has Expired.");
        }
        user.setActive(true);
        userRepository.save(user);
        return new MessageDto("Customer Account Activated.");
    }

    public MessageDto resendActivationLink(String email) {
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if(customer==null){
            throw new UserNotFoundException("Customer Not Found");
        }
        if(customer.isActive()){
            throw new AlreadyExistsException("Account Already Activated");
        }
        String token = UUID.randomUUID().toString();
        customer.setActivationToken(token);
        customer.setActivationTokenExpiry(new Date(new Date().getTime()+resetExpiry));
        customerRepository.save(customer);
        String body = "http://localhost:8080/customer/activate?token="+token;
        emailSenderService.sendEmail(email,body,"Re-Activation Link for your account");
        return new MessageDto("Re-Activation Link Sended Successfully.");
    }

    public CustomerResponseDto viewProfile(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(username).orElse(null);
        if(customer==null){
            throw new UserNotFoundException("User Not Found");
        }
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        customerResponseDto.setId(customer.getId());
        customerResponseDto.setFirstName(customer.getFirstName());
        customerResponseDto.setLastName(customer.getLastName());
        customerResponseDto.setFullName(customer.getFirstName()+" "+customer.getLastName());
        customerResponseDto.setEmail(customer.getEmail());
        customerResponseDto.setIsActive(customer.isActive());
        customerResponseDto.setContact(customer.getContact());
        customerResponseDto.setCreated_by(customer.getCreatedBy());
        customerResponseDto.setDate_created(customer.getDateCreated());
        customerResponseDto.setLast_updated(customer.getLastUpdated());
        customerResponseDto.setUpdated_by(customer.getUpdatedBy());
        return customerResponseDto;
    }

    public MessageDto updateProfile(CustomerDto customerDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(username).orElse(null);
        if(customer==null){
            throw new UserNotFoundException("Customer Not Found");
        }
        if(customerDto.getFirstName()!=null) customer.setFirstName(customerDto.getFirstName());
        if(customerDto.getMiddleName()!=null) customer.setMiddleName(customerDto.getMiddleName());
        if(customerDto.getLastName()!=null) customer.setLastName(customerDto.getLastName());
        if(customerDto.getContact()!=null) customer.setContact(customerDto.getContact());
        customer.setUpdatedBy(username);
        customerRepository.save(customer);
        return new MessageDto("Customer Updated Successfully");
    }

    public MessageDto updatePassword(UpdatePasswordDto updatePasswordDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(username).orElse(null);
        if(customer==null){
            throw new UserNotFoundException("Customer Not Found");
        }
        if(!updatePasswordDto.getPassword().equals(updatePasswordDto.getConfirmPassword())){
            throw new PasswordMismatchException("Confirm Password Doesn't Match");
        }
        customer.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
        customer.setUpdatedBy(customer.getEmail());
        customerRepository.save(customer);
        emailSenderService.sendEmail(username,"Your Password Has Been Changed Successfully.","Password Changed");
        return new MessageDto("Customer Password Updated Successfully");
    }

    public MessageDto deleteAddress(Long id) {
        Address address = addressRepository.findById(id).orElse(null);
        if(address==null || !address.getCustomer().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            throw new UserNotFoundException("Address Id Not Found");
        }
        addressRepository.deleteById(id);
        return new MessageDto("Address Deleted Successfully.");
    }

    public List<Address> viewAddresses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if(customer==null){
            throw new UserNotFoundException("Customer Not Found");
        }
        return addressRepository.findByCustomerId(customer.getId());
    }

    public MessageDto updateAddress(Long id, AddressDto addressDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Address address = addressRepository.findById(id).orElse(null);
        Customer customer = customerRepository.findByEmail(username).orElse(null);
        if(address==null || customer==null || !customer.getId().equals(address.getCustomer().getId())){
            throw new UserNotFoundException("Address Id Not Found");
        }
        if (addressDto.getCity() != null) address.setCity(addressDto.getCity());
        if(addressDto.getState()!=null) address.setState(addressDto.getState());
        if(addressDto.getCountry()!=null) address.setCountry(addressDto.getCountry());
        if(addressDto.getAddress_line()!=null) address.setAddress_line(addressDto.getAddress_line());
        if(addressDto.getZip_code()!=null) address.setZip_code(addressDto.getZip_code());
        if(addressDto.getLabel()!=null) address.setLabel(addressDto.getLabel());
        addressRepository.save(address);

        customer.setUpdatedBy(customer.getEmail());
        customerRepository.save(customer);
        return new MessageDto("Address Updated Successfully.");
    }

    public MessageDto addAddress(AddressDto addressDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if(customer==null) {
            throw new UserNotFoundException("Customer Not Found");
        }
        Address address = new Address();
        address.setId(addressDto.getId());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setAddress_line(addressDto.getAddress_line());
        address.setZip_code(addressDto.getZip_code());
        address.setLabel(addressDto.getLabel());
        address.setCustomer(customer);
        addressRepository.save(address);

        customer.getAddresses().add(address);
        customer.setUpdatedBy(customer.getEmail());
        customerRepository.save(customer);
        return new MessageDto("Address Added Successfully.");
    }
}
