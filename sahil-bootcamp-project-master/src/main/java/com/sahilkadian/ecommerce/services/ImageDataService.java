package com.sahilkadian.ecommerce.services;

import com.sahilkadian.ecommerce.entities.ImageData;
import com.sahilkadian.ecommerce.repositories.ImageDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class ImageDataService {

    @Autowired
    private ImageDataRepository imageDataRepository;

    @Value("${base.path}")
    private String BASE_PATH;

    public String uploadUserImage(MultipartFile file) throws IOException{
        String filepath = BASE_PATH+file.getOriginalFilename();

        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filepath)
                .build());

        file.transferTo(new File(filepath));
        return "File Uploaded Successfully :"+filepath;
    }

    public byte[] downloadUserImage(String fileName) throws IOException{
        Optional<ImageData> imageData = imageDataRepository.findByName(fileName);
        String filePath = imageData.get().getFilePath();
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }

    public ResponseEntity<String> uploadProductImage(MultipartFile file) throws IOException{
        String filepath = BASE_PATH+"/products/"+file.getOriginalFilename();

        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filepath)
                .build());

        return new ResponseEntity<>("USER IMAGE UPLOADED", HttpStatus.OK);
    }

    public ResponseEntity<String> uploadProductVariationImage(MultipartFile file) throws IOException{
        String filepath = BASE_PATH+"/products/"+file.getOriginalFilename();

        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filepath)
                .build());

        return new ResponseEntity<>("USER IMAGE UPLOADED", HttpStatus.OK);
    }
}
