package com.example.appointmentscheduler.service;

import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.entity.ChatMessage;
import com.example.appointmentscheduler.entity.ExchangeRequest;
import com.example.appointmentscheduler.entity.Invoice;
import org.thymeleaf.context.Context;

import java.io.File;

public interface EmailService {
    void sendEmail(String to, String subject, String templateName, Context templateContext, File attachment);

    void sendAppointmentFinishedNotification(Appointment appointment);

    void sendAppointmentRejectionRequestedNotification(Appointment appointment);

    void sendNewAppointmentScheduledNotification(Appointment appointment);

    void sendAppointmentCanceledByCustomerNotification(Appointment appointment);

    void sendAppointmentCanceledByDoctorNotification(Appointment appointment);

    void sendInvoice(Invoice invoice);

    void sendAppointmentRejectionAcceptedNotification(Appointment appointment);

    void sendNewChatMessageNotification(ChatMessage appointment);

    //DELETE: xóa trao đổi lịch hẹn

//    void sendNewExchangeRequestedNotification(Appointment oldAppointment, Appointment newAppointment);
//
//    void sendExchangeRequestAcceptedNotification(ExchangeRequest exchangeRequest);
//
//    void sendExchangeRequestRejectedNotification(ExchangeRequest exchangeRequest);
}
