package com.example.appointmentscheduler.service.impl;


import com.example.appointmentscheduler.service.BarcodeService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;


@Service
public class BarcodeServiceImpl implements BarcodeService {
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
    public String processImage(byte[] imageData) {
        try {

            // Kiểm tra dữ liệu hình ảnh có null không
            if (imageData == null || imageData.length == 0) {
                return "Dữ liệu hình ảnh không hợp lệ";
            }

            // Chuyển đổi dữ liệu hình ảnh từ byte array sang BufferedImage
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));

            // Kiểm tra xem BufferedImage có null không
            if (bufferedImage == null) {
                return "Không thể đọc hình ảnh";
            }
            // Chuyển đổi BufferedImage thành JavaFX Image
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            // Tạo đối tượng LuminanceSource từ BufferedImage
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            HybridBinarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            // Thiết lập các thông số cho MultiFormatReader
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            // Sử dụng MultiFormatReader để đọc mã barcode từ hình ảnh
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap, hints);

            // Trả về dữ liệu của mã barcode
            return result.getText();
        } catch (IOException | com.google.zxing.NotFoundException e) {
            e.printStackTrace();
            // Trong trường hợp xảy ra lỗi hoặc không tìm thấy mã barcode, trả về null hoặc một giá trị khác để xử lý
            return null;
        }
    }
}
