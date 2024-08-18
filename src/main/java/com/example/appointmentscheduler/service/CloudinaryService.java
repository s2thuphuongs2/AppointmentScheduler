package com.example.appointmentscheduler.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    Map<String, Object> uploadBarcodeImage(byte[] imageBytes, String barcodeId);

    Map<String, Object> uploadQRCodeImage(byte[] imageBytes, String barcodeId);
}
