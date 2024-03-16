package com.example.appointmentscheduler.service;

import com.example.appointmentscheduler.entity.Appointment;

import java.time.LocalDateTime;
import java.util.Date;

public interface JwtTokenService {
    String generateAppointmentRejectionToken(Appointment appointment);

    String generateAcceptRejectionToken(Appointment appointment);

    boolean validateToken(String token);

    int getAppointmentIdFromToken(String token);

    int getCustomerIdFromToken(String token);

    int getDoctorIdFromToken(String token);

    ////
    Date convertLocalDateTimeToDate(LocalDateTime localDateTime);
}
