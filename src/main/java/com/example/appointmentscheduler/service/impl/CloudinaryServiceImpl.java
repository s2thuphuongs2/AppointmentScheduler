package com.example.appointmentscheduler.service.impl;

import com.cloudinary.Cloudinary;
import com.example.appointmentscheduler.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService{
    private final Cloudinary cloudinary;

//    @Override
//    public Map upload(MultipartFile file) {
//        try{
//            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
//            return data;
//        }catch (IOException io){
//            throw new RuntimeException("Image upload fail");
//        }
//    }
    @Override
    public Map<String, Object> uploadBarcodeImage(byte[] imageBytes, Long barcodeId) {
        try {
            Map<String, Object> params = Map.of(
                    "public_id", "barcodes/"+barcodeId,
                    "overwrite", true,
                    "notification_url", "",
                    "resource_type", "image",
                    "file", imageBytes,
                    "folder", "barcodes"
            );
            return cloudinary.uploader().upload(imageBytes, Map.of());
        } catch (IOException e) {
            throw new RuntimeException("Image upload fail");
        }
    }
}
