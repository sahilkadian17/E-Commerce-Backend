package com.sahilkadian.ecommerce.repositories;

import com.sahilkadian.ecommerce.entities.TokenStore;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenStore,Long> {

    Optional<TokenStore> findByToken(String token);

    Boolean existsByToken(String token);

    @Transactional
    void deleteByEmail(String username);

    Optional<TokenStore> findByEmail(String email);

    boolean existsByUserId(Long id);

    Optional<TokenStore> findByUserId(Long id);
}
