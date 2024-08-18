package com.example.appointmentscheduler.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.dao.BarcodeRepository;
import com.example.appointmentscheduler.service.AppointmentService;
import com.example.appointmentscheduler.service.BarcodeService;
import com.example.appointmentscheduler.service.CloudinaryService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

@Service
public class BarcodeServiceImpl implements BarcodeService {
    @Autowired
    private BarcodeRepository barcodeRepository;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Override
    public byte[] genarateBarcodeImage(Long barcodeId) throws WriterException, IOException {
            String barcodeContent = String.valueOf(barcodeId);
            //Kích thước của hình ảnh mã vạch tính bằng pixels
            int width = 300;
            int height = 100;
            // Tạo đối tượng BitMatrix để mã hóa mã vạch
            BitMatrix matrix = new MultiFormatWriter().encode(barcodeContent, BarcodeFormat.CODE_128, width, height);
            // Tạo hình ảnh từ BitMatrix với mỗi pixel có màu đen hoặc trắng, dữ liệu này được lưu trữ trong mảng byte
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //Phương thức setRGB() dat mau tung pixel dua tren bit tuong ung trong matran có được đặt hay không?
            // Nếu được đặt, pixel là màu đen, nếu không thì màu trắng
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, matrix.get(x, y) ? 0 : 0xFFFFFF);
                }
            }
            // MANUAL: fix bug ảnh không hiện lần đầu
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.out.println("image: " + image);
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
    }


    @Override
    public String generateBarcodeImageAndSave(byte[] barcodeImageBytes, Long barcodeId) throws WriterException, IOException {
        // Tải ảnh mã vạch lên Cloudinary và lấy URL
        String barcodeIdString = String.valueOf(barcodeId);
        Map<String, Object> uploadResult = cloudinaryService.uploadBarcodeImage(barcodeImageBytes, barcodeIdString);
        String barcodeImageUrl = (String) uploadResult.get("url");
        return barcodeImageUrl;
    }
@Override
    public long generate9DigitBarcode() {
        Random random = new Random();
        return 100_000_000L + random.nextInt(900_000_000);
    }
    @Override
    public String scanBarcode(String barcode) {
        Long barcodeId = Long.parseLong(barcode);
        Appointment appointment = barcodeRepository.findByBarcodeId(barcodeId);
        if (appointment != null) {
            appointmentService.updateAppointmentStatusAfterBarcodeScan(barcode);
            return "appointments/" + appointment.getId();
        } else {
            return "Không tìm thấy thông tin liên quan đến mã vạch " + barcodeId;
        }
    }
}
