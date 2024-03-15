package com.example.appointmentscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class BarcodeScannerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

}
