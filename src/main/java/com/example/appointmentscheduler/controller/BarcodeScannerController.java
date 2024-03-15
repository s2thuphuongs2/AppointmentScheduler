package com.example.appointmentscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.appointmentscheduler.service.BarcodeService;

import java.util.Map;

import static org.springframework.web.servlet.function.ServerResponse.badRequest;

@Controller
public class BarcodeScannerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BarcodeService barcodeService;

    @GetMapping("/scan/{barcode}")
    @ResponseBody
    public String scanBarcode(@PathVariable String barcode) {
        //Xử lý dữ liệu từ thiết bị quét barcode

        // Truy vấn cơ sở dữ liệu để tìm kiếm bản ghi trong bảng Appointment dựa trên barcode_id
        Map<String, Object> appointments = jdbcTemplate.queryForMap(
                "SELECT * FROM appointments WHERE barcode_id = ?", barcode);

        if (appointments != null) {
        // Nếu tìm thấy data tương ứng, điều hướng người dùng tới trang /appointment/{id} của bảng đó
            int id = (int) appointments.get("id");
            return "appointments/" + id;
        } else {
        // Nếu không tìm thấy data tương ứng, thông báo lỗi
            return "Không tìm thấy thông tin liên quan đến mã vạch " + barcode;
        }

    }
    @PostMapping("/process-image")
//    @ResponseBody
    public ResponseEntity<?> processImage(@RequestBody byte[] imageData) {

        // Sau khi tìm được mã barcode, gửi dữ liệu barcode đến server để xử lý
        // Ví dụ: scanBarcode(barcodeData);
        if (imageData != null) {
            // Xử lý hình ảnh từ camera để tìm mã barcode
            String barcodeData =  barcodeService.processImage(imageData);
            if (barcodeData != null) {
                // Nếu tìm thấy mã barcode, trả về dữ liệu đó
//                return  barcodeData;
                return ResponseEntity.ok(barcodeData);
            } else {
                // Nếu không tìm thấy mã barcode hoặc xảy ra lỗi, trả về thông báo lỗi
//                return "Không tìm thấy mã barcode hoặc xảy ra lỗi khi xử lý hình ảnh.";
                return ResponseEntity.badRequest().body("Không tìm thấy mã barcode hoặc xảy ra lỗi khi xử lý hình ảnh.");
            }

        } else {
//            return "Không tìm thấy mã barcode trong hình ảnh";
            return ResponseEntity.badRequest().body("Không tìm thấy mã barcode trong hình ảnh");
        }
    }

}
