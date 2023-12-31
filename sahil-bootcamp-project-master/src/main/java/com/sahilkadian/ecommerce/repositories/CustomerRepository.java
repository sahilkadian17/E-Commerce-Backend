package com.sahilkadian.ecommerce.repositories;

import com.sahilkadian.ecommerce.entities.Customer;
import com.sahilkadian.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByEmail(String email);

    Boolean existsByEmail(String email);
}