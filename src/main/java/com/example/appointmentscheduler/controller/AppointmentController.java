package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.entity.ChatMessage;
import com.example.appointmentscheduler.security.CustomUserDetails;
import com.example.appointmentscheduler.service.AppointmentService;
import com.example.appointmentscheduler.service.UserService;
import com.example.appointmentscheduler.service.WorkService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private static final String REJECTION_CONFIRMATION_VIEW = "appointments/rejectionConfirmation";

    private final WorkService workService;
    private final UserService userService;
    private final AppointmentService appointmentService;


    public AppointmentController(WorkService workService, UserService userService, AppointmentService appointmentService) {
        this.workService = workService;
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/all")
    public String showAllAppointments(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        String appointmentsModelName = "appointments";

        if (currentUser.hasRole("ROLE_CUSTOMER")) {
            model.addAttribute(appointmentsModelName, appointmentService.getAppointmentByCustomerId(currentUser.getId()));
        } else if (currentUser.hasRole("ROLE_PROVIDER")) {
            model.addAttribute(appointmentsModelName, appointmentService.getAppointmentByDoctorId(currentUser.getId()));
        } else if (currentUser.hasRole("ROLE_ADMIN")) {
            model.addAttribute(appointmentsModelName, appointmentService.getAllAppointments());
        }
        return "appointments/listAppointments";
    }

    @GetMapping("/{id}")
    public String showAppointmentDetail(@PathVariable("id") int appointmentId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        Appointment appointment = appointmentService.getAppointmentByIdWithAuthorization(appointmentId);
        model.addAttribute("appointment", appointment);
        model.addAttribute("chatMessage", new ChatMessage());
        boolean allowedToRequestRejection = appointmentService.isCustomerAllowedToRejectAppointment(currentUser.getId(), appointmentId);
        boolean allowedToAcceptRejection = appointmentService.isDoctorAllowedToAcceptRejection(currentUser.getId(), appointmentId);
       ////DELETE
//        boolean allowedToExchange = exchangeService.checkIfEligibleForExchange(currentUser.getId(), appointmentId);
        model.addAttribute("allowedToRequestRejection", allowedToRequestRejection);
        model.addAttribute("allowedToAcceptRejection", allowedToAcceptRejection);
//        model.addAttribute("allowedToExchange", allowedToExchange);
        if (allowedToRequestRejection) {
            model.addAttribute("remainingTime", formatDuration(Duration.between(LocalDateTime.now(), appointment.getEnd().plusDays(1))));
        }
        String cancelNotAllowedReason = appointmentService.getCancelNotAllowedReason(currentUser.getId(), appointmentId);
        model.addAttribute("allowedToCancel", cancelNotAllowedReason == null);
        model.addAttribute("cancelNotAllowedReason", cancelNotAllowedReason);
        return "appointments/appointmentDetail";
    }


    @PostMapping("/reject")
    public String processAppointmentRejectionRequest(@RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        boolean result = appointmentService.requestAppointmentRejection(appointmentId, currentUser.getId());
        model.addAttribute(result);
        model.addAttribute("type", "request");
        return REJECTION_CONFIRMATION_VIEW;
    }

    @GetMapping("/reject")
    public String processAppointmentRejectionRequest(@RequestParam("token") String token, Model model) {
        boolean result = appointmentService.requestAppointmentRejection(token);
        model.addAttribute(result);
        model.addAttribute("type", "request");
        return REJECTION_CONFIRMATION_VIEW;
    }

    @PostMapping("/acceptRejection")
    public String acceptAppointmentRejectionRequest(@RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        boolean result = appointmentService.acceptRejection(appointmentId, currentUser.getId());
        model.addAttribute(result);
        model.addAttribute("type", "accept");
        return REJECTION_CONFIRMATION_VIEW;
    }

    @GetMapping("/acceptRejection")
    public String acceptAppointmentRejectionRequest(@RequestParam("token") String token, Model model) {
        boolean result = appointmentService.acceptRejection(token);
        model.addAttribute(result);
        model.addAttribute("type", "accept");
        return REJECTION_CONFIRMATION_VIEW;
    }

    @PostMapping("/messages/new")
    public String addNewChatMessage(@ModelAttribute("chatMessage") ChatMessage chatMessage, @RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        int authorId = currentUser.getId();
        appointmentService.addMessageToAppointmentChat(appointmentId, authorId, chatMessage);
        return "redirect:/appointments/" + appointmentId;
    }

    @GetMapping("/new")
    public String selectDoctor(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.hasRole("ROLE_CUSTOMER_RETAIL")) {
            model.addAttribute("doctors", userService.getDoctorsWithRetailWorks());
        } else if (currentUser.hasRole("ROLE_CUSTOMER_CORPORATE")) {
            model.addAttribute("doctors", userService.getDoctorsWithCorporateWorks());
        }
        return "appointments/selectDoctor";
    }

    @GetMapping("/new/{doctorId}")
    public String selectService(@PathVariable("doctorId") int doctorId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.hasRole("ROLE_CUSTOMER_RETAIL")) {
            model.addAttribute("works", workService.getWorksForRetailCustomerByDoctorId(doctorId));
        } else if (currentUser.hasRole("ROLE_CUSTOMER_CORPORATE")) {
            model.addAttribute("works", workService.getWorksForCorporateCustomerByDoctorId(doctorId));
        }
        model.addAttribute(doctorId);
        return "appointments/selectService";
    }

    @GetMapping("/new/{doctorId}/{workId}")
    public String selectDate(@PathVariable("workId") int workId, @PathVariable("doctorId") int doctorId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (workService.isWorkForCustomer(workId, currentUser.getId())) {
            model.addAttribute(doctorId);
            model.addAttribute("workId", workId);
            return "appointments/selectDate";
        } else {
            return "redirect:/appointments/new";
        }

    }

    @GetMapping("/new/{doctorId}/{workId}/{dateTime}")
    public String showNewAppointmentSummary(@PathVariable("workId") int workId, @PathVariable("doctorId") int doctorId, @PathVariable("dateTime") String start, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (appointmentService.isAvailable(workId, doctorId, currentUser.getId(), LocalDateTime.parse(start))) {
            model.addAttribute("work", workService.getWorkById(workId));
            model.addAttribute("doctor", userService.getDoctorById(doctorId).getFirstName() + " " + userService.getDoctorById(doctorId).getLastName());
            model.addAttribute(doctorId);
            model.addAttribute("start", LocalDateTime.parse(start));
            model.addAttribute("end", LocalDateTime.parse(start).plusMinutes(workService.getWorkById(workId).getDuration()));
            return "appointments/newAppointmentSummary";
        } else {
            return "redirect:/appointments/new";
        }
    }

    @PostMapping("/new")
    public String bookAppointment(@RequestParam("workId") int workId, @RequestParam("doctorId") int doctorId, @RequestParam("start") String start, @AuthenticationPrincipal CustomUserDetails currentUser) {
        appointmentService.createNewAppointment(workId, doctorId, currentUser.getId(), LocalDateTime.parse(start));
        return "redirect:/appointments/all";
    }

    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        appointmentService.cancelUserAppointmentById(appointmentId, currentUser.getId());
        return "redirect:/appointments/all";
    }

    public static String formatDuration(Duration duration) {
        long s = duration.getSeconds();
        return String.format("%dh%02dm", s / 3600, (s % 3600) / 60);
    }

}
