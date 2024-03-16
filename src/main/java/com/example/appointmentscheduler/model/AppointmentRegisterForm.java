package com.example.appointmentscheduler.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


public class AppointmentRegisterForm {

    private int workId;
    private int doctorId;
    private int customerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end;

    public AppointmentRegisterForm() {
    }

    public AppointmentRegisterForm(int workId, int doctorId, LocalDateTime start, LocalDateTime end) {
        this.workId = workId;
        this.doctorId = doctorId;
        this.start = start;
        this.end = end;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
