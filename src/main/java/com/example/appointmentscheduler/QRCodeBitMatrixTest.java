package com.example.appointmentscheduler;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeBitMatrixTest {

    public static void main(String[] args) {
        try {
            String inputData = "HELLO WORLD";
            int width = 300;
            int height = 300;

            BitMatrix bitMatrix = new MultiFormatWriter().encode(inputData, BarcodeFormat.QR_CODE, width, height);

            // In ra ma trận BitMatrix
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    System.out.print(bitMatrix.get(x, y) ? "1" : "0");
                }
                System.out.println();
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
