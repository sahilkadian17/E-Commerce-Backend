package com.sahilkadian.ecommerce.repositories;

import com.sahilkadian.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByActivationToken(String token);

    Optional<User> findByResetPasswordToken(String token);
}
