package com.sahilkadian.ecommerce.repositories;

import com.sahilkadian.ecommerce.entities.Seller;
import com.sahilkadian.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {

    Optional<Seller> findByEmail(String email);

    Boolean existsByEmail(String email);
}