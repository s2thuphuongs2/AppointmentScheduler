package com.example.appointmentscheduler.service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface BarcodeService {
    byte[] genarateBarcodeImage(Long barcodeContent) throws WriterException, IOException;

    String scanBarcode(String barcodeId);

    String generateBarcodeImageAndSave(byte[] barcodeImageBytes, Long barcodeId) throws WriterException, IOException;

    long generate9DigitBarcode();
}
