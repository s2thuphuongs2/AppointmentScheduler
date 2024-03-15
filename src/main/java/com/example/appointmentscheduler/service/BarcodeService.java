package com.example.appointmentscheduler.service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface BarcodeService {
    byte[] genarateBarcodeImage(Long barcodeContent) throws WriterException, IOException;

    String generateBarcodeImageAndSave(Long barcodeContent) throws WriterException, IOException;

    String processImage(byte[] imageData);

}
