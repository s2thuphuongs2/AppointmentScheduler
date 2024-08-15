package com.example.appointmentscheduler.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
//    Map upload(MultipartFile file);

    //    @Override
    //    public Map upload(MultipartFile file) {
    //        try{
    //            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
    //            return data;
    //        }catch (IOException io){
    //            throw new RuntimeException("Image upload fail");
    //        }
    //    }
    Map<String, Object> uploadBarcodeImage(byte[] imageBytes, Long barcodeId);
}
