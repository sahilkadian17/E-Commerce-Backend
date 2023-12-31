package com.sahilkadian.ecommerce.services;

import com.sahilkadian.ecommerce.dto.CustomerResponseDto;
import com.sahilkadian.ecommerce.dto.MessageDto;
import com.sahilkadian.ecommerce.dto.SellerResponseDto;
import com.sahilkadian.ecommerce.entities.Address;
import com.sahilkadian.ecommerce.entities.Customer;
import com.sahilkadian.ecommerce.entities.Seller;
import com.sahilkadian.ecommerce.entities.User;
import com.sahilkadian.ecommerce.exceptions.AlreadyExistsException;
import com.sahilkadian.ecommerce.exceptions.UserNotFoundException;
import com.sahilkadian.ecommerce.repositories.CustomerRepository;
import com.sahilkadian.ecommerce.repositories.SellerRepository;
import com.sahilkadian.ecommerce.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    public List<CustomerResponseDto> getAllCustomers(int pageSize,int pageOffset,String sortedBy,String sortDirection){
        PageRequest pageRequest = PageRequest.of(pageOffset,pageSize, Sort.Direction.fromString(sortDirection),sortedBy);
        Page<Customer> customers = customerRepository.findAll(pageRequest);
        List<CustomerResponseDto> customersList = new ArrayList<>();
        for(Customer customer:customers){
            CustomerResponseDto customerResponseDto = new CustomerResponseDto();
            customerResponseDto.setId(customer.getId());
            customerResponseDto.setFirstName(customer.getFirstName());
            customerResponseDto.setLastName(customer.getLastName());
            customerResponseDto.setFullName(customer.getFirstName()+" "+ customer.getLastName());
            customerResponseDto.setEmail(customer.getEmail());
            customerResponseDto.setIsActive(customer.isActive());
            customerResponseDto.setContact(customer.getContact());
            customerResponseDto.setCreated_by(customer.getCreatedBy());
            customerResponseDto.setDate_created(customer.getDateCreated());
            customerResponseDto.setUpdated_by(customer.getUpdatedBy());
            customerResponseDto.setLast_updated(customer.getLastUpdated());
            customersList.add(customerResponseDto);
        }
        return customersList;
    }

    public List<SellerResponseDto> getAllSellers(int pageSize,int pageOffset,String sortedBy,String sortDirection){
        PageRequest pageable = PageRequest.of(pageOffset,pageSize, Sort.Direction.fromString(sortDirection),sortedBy);
        Page<Seller> sellers = sellerRepository.findAll(pageable);
        List<SellerResponseDto> sellersList = new ArrayList<>();
        for(Seller seller:sellers){
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
            sellerResponseDto.setUpdated_by(seller.getUpdatedBy());
            sellerResponseDto.setLast_updated(seller.getLastUpdated());

            Address address = new Address();
            address.setId(seller.getAddress().getId());
            address.setCity(seller.getAddress().getCity());
            address.setState(seller.getAddress().getState());
            address.setCountry(seller.getAddress().getCountry());
            address.setAddress_line(seller.getAddress().getAddress_line());
            address.setZip_code(seller.getAddress().getZip_code());
            address.setLabel(seller.getAddress().getLabel());

            sellerResponseDto.setCompanyAddress(address);
            sellersList.add(sellerResponseDto);
        }
        return sellersList;
    }

    public MessageDto activateDeactivateUser(Long id, Boolean active){
        User user = userRepository.findById(id).orElse(null);
        if(user==null) {
            throw new UserNotFoundException("User Not Found");
        }
        if(active){
            if(!user.isActive()){
                user.setActive(true);
                user.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                userRepository.save(user);
                emailSenderService.sendEmail(user.getEmail(),"Your Account Has Been Activated Successfully.","Account Activated");
                return new MessageDto("Account Activated Successfully.");
            }
            else
                throw new AlreadyExistsException("Account Already Activated");
        }
        else{
            if(user.isActive()){
                user.setActive(false);
                user.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                userRepository.save(user);
                emailSenderService.sendEmail(user.getEmail(),"Your Account Has Been Deactivated Successfully.","Account Deactivated");
                return new MessageDto("Account Deactivated Successfully.");
            }
            else
                throw new AlreadyExistsException("Account Already Deactivated");
        }
    }

    public MessageDto lockUnlockUser(Long id,Boolean lock){
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            throw new UserNotFoundException("User Not Found");
        }
        if(lock){
            if(!user.isLocked()){
                user.setLocked(true);
                user.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                userRepository.save(user);
                return new MessageDto("User Account Locked Successfully");
            }
            throw new AlreadyExistsException("User Account Already Locked");
        }
        else{
            if(user.isLocked()){
                user.setLocked(false);
                user.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                userRepository.save(user);
                return new MessageDto("User Account Unlocked Successfully");
            }
            throw new AlreadyExistsException("User Account Already Unlocked");
        }
    }
}
