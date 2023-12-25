package com.example.appointmentscheduler.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Đối tượng biểu diễn một biểu mẫu đăng ký lịch hẹn.
 */
public class AppointmentRegisterForm {

    // ID công việc
    private int workId;

    // ID người cung cấp dịch vụ
    private int providerId;

    // ID khách hàng
    private int customerId;

    // Thời điểm bắt đầu lịch hẹn
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    // Thời điểm kết thúc lịch hẹn
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end;

    /**
     * Constructor mặc định.
     */
    public AppointmentRegisterForm() {
    }

    /**
     * Constructor với các tham số cần thiết.
     *
     * @param workId     ID công việc.
     * @param providerId ID người cung cấp dịch vụ.
     * @param customerId ID khách hàng.
     */
    public AppointmentRegisterForm(int workId, int providerId, int customerId) {
        this.workId = workId;
        this.providerId = providerId;
        this.customerId = customerId;
    }

    /**
     * Phương thức để lấy ID công việc.
     *
     * @return ID công việc.
     */
    public int getWorkId() {
        return workId;
    }

    /**
     * Phương thức để thiết lập ID công việc.
     *
     * @param workId ID công việc cần thiết lập.
     */
    public void setWorkId(int workId) {
        this.workId = workId;
    }

    /**
     * Phương thức để lấy thời điểm bắt đầu lịch hẹn.
     *
     * @return Thời điểm bắt đầu lịch hẹn.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Phương thức để thiết lập thời điểm bắt đầu lịch hẹn.
     *
     * @param start Thời điểm bắt đầu lịch hẹn cần thiết lập.
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Phương thức để lấy thời điểm kết thúc lịch hẹn.
     *
     * @return Thời điểm kết thúc lịch hẹn.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Phương thức để thiết lập thời điểm kết thúc lịch hẹn.
     *
     * @param end Thời điểm kết thúc lịch hẹn cần thiết lập.
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
