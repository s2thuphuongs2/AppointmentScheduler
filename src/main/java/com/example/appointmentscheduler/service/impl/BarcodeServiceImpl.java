package com.example.appointmentscheduler.service.impl;

import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.dao.BarcodeRepository;
import com.example.appointmentscheduler.service.BarcodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;

@Service
public class BarcodeServiceImpl implements BarcodeService {
    @Autowired
    private BarcodeRepository barcodeRepository;

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
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.out.println("image: " + image);
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();




    }
    @Override
    public String generateBarcodeImageAndSave(Long barcodeIdNum) throws WriterException, IOException {
        byte[] barcodeImageBytes = genarateBarcodeImage(barcodeIdNum);
        String imagePath = saveImageToFile(barcodeIdNum, barcodeImageBytes);
        return imagePath;
    }
    private String saveImageToFile(Long barcodeIdNum, byte[] imageBytes) throws IOException {
        String barcodeId = String.valueOf(barcodeIdNum);
        String imagePath = "src/main/resources/static/img/barcodes/" + barcodeId + ".png";
        // Lưu byte array vào file ảnh
        // (Bạn có thể sử dụng java.nio.file.Files hoặc các thư viện hỗ trợ để thực hiện việc này)
        // Ví dụ: Files.write(Paths.get(imagePath), imageBytes);
        File imageFile = new File(imagePath);
        Files.write(Paths.get(imageFile.getAbsolutePath()), imageBytes);
        return imagePath;
    }

    public long generate9DigitBarcode() {
        Random random = new Random();
        return 100_000_000L + random.nextInt(900_000_000);
    }
    @Override
    public String scanBarcode(String barcode) {
        Long barcodeId = Long.parseLong(barcode);
        Appointment appointment = barcodeRepository.findByBarcodeId(barcodeId);
        if (appointment != null) {
            return "appointments/" + appointment.getId();
        } else {
            return "Không tìm thấy thông tin liên quan đến mã vạch " + barcodeId;
        }
        // Note: Các bước
        //        //Xử lý dữ liệu từ thiết bị quét barcode
//
//        // Truy vấn cơ sở dữ liệu để tìm kiếm bản ghi trong bảng Appointment dựa trên barcode_id
//        Map<String, Object> appointments = jdbcTemplate.queryForMap(
//                "SELECT * FROM appointments WHERE barcode_id = ?", barcode);
//
//        if (appointments != null) {
//        // Nếu tìm thấy data tương ứng, điều hướng người dùng tới trang /appointment/{id} của bảng đó
//            int id = (int) appointments.get("id");
//            return "appointments/" + id;
//        } else {
//        // Nếu không tìm thấy data tương ứng, thông báo lỗi
//            return "Không tìm thấy thông tin liên quan đến mã vạch " + barcode;
//        }
    }
}
