package com.sahilkadian.ecommerce.repositories;

import com.sahilkadian.ecommerce.entities.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData,Long> {
    Optional<ImageData> findByName(String fileName);
}
