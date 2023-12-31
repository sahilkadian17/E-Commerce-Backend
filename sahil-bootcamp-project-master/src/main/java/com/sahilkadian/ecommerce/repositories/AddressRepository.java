package com.sahilkadian.ecommerce.repositories;

import com.sahilkadian.ecommerce.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {

    boolean existsById(Long id);

    void deleteById(Long id);

    Optional<Address> findById(Long id);

    List<Address> findByCustomerId(Long id);
}
