package com.example.appointmentscheduler.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.appointmentscheduler.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService{
    private final Cloudinary cloudinary;

    @Override
    public Map<String, Object> uploadBarcodeImage(byte[] imageBytes, String barcodeId) {
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

    @Override
    public Map<String, Object> uploadQRCodeImage(byte[] imageBytes, String qrcodeName) {
        try {
            Map<String, Object> params = Map.of(
                    "public_id", "qrcodes" + qrcodeName,
                    "overwrite", true,
                    "notification_url", "",
                    "resource_type", "image",
                    "folder", "qrcodes",
                    "invalidate", true
            );

            // Upload ảnh lên Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(imageBytes, params);
            // Lên lịch xóa ảnh sau 60 giây
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                try {
                    cloudinary.uploader().destroy("qrcodes/" + "qrcodes"+ qrcodeName, Map.of());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 60, TimeUnit.SECONDS);

            return uploadResult;
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }


}
