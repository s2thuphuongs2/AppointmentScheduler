package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.appointmentscheduler.security.CustomUserDetails;
import com.example.appointmentscheduler.service.AppointmentService;
import com.example.appointmentscheduler.service.NotificationService;
import com.google.common.collect.Lists;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class AjaxController {

    private final AppointmentService appointmentService;
    private final NotificationService notificationService;

    public AjaxController(AppointmentService appointmentService, NotificationService notificationService) {
        this.appointmentService = appointmentService;
        this.notificationService = notificationService;
    }


    @GetMapping("/user/{userId}/appointments")
    public List<Appointment> getAppointmentsForUser(@PathVariable("userId") int userId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.hasRole("ROLE_CUSTOMER")) {
            return appointmentService.getAppointmentByCustomerId(userId);
        } else if (currentUser.hasRole("ROLE_PROVIDER"))
            return appointmentService.getAppointmentByDoctorId(userId);
        else if (currentUser.hasRole("ROLE_ADMIN"))
            return appointmentService.getAllAppointments();
        else return Lists.newArrayList();
    }

    @GetMapping("/availableHours/{doctorId}/{workId}/{date}")
    public List<AppointmentRegisterForm> getAvailableHours(@PathVariable("doctorId") int doctorId, @PathVariable("workId") int workId, @PathVariable("date") String date, @AuthenticationPrincipal CustomUserDetails currentUser) {
        LocalDate localDate = LocalDate.parse(date);
        return appointmentService.getAvailableHours(doctorId, currentUser.getId(), workId, localDate)
                .stream()
                .map(timePeriod -> new AppointmentRegisterForm(workId, doctorId, timePeriod.getStart().atDate(localDate), timePeriod.getEnd().atDate(localDate)))
                .collect(Collectors.toList());
    }

    @GetMapping("/user/notifications")
    public int getUnreadNotificationsCount(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return notificationService.getUnreadNotifications(currentUser.getId()).size();
    }

}
