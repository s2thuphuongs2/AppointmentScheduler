package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.entity.ChatMessage;
import com.example.appointmentscheduler.security.CustomUserDetails;
import com.example.appointmentscheduler.service.*;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            model.addAttribute(appointmentsModelName, appointmentService.getAppointmentByProviderId(currentUser.getId()));
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
        boolean allowedToAcceptRejection = appointmentService.isProviderAllowedToAcceptRejection(currentUser.getId(), appointmentId);
        model.addAttribute("allowedToRequestRejection", allowedToRequestRejection);
        model.addAttribute("allowedToAcceptRejection", allowedToAcceptRejection);
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
    public String selectProvider(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.hasRole("ROLE_CUSTOMER_RETAIL")) {
            model.addAttribute("providers", userService.getProvidersWithRetailWorks());
        } else if (currentUser.hasRole("ROLE_CUSTOMER_CORPORATE")) {
            model.addAttribute("providers", userService.getProvidersWithCorporateWorks());
        }
        return "appointments/selectProvider";
    }

    @GetMapping("/new/{providerId}")
    public String selectService(@PathVariable("providerId") int providerId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.hasRole("ROLE_CUSTOMER_RETAIL")) {
            model.addAttribute("works", workService.getWorksForRetailCustomerByProviderId(providerId));
        } else if (currentUser.hasRole("ROLE_CUSTOMER_CORPORATE")) {
            model.addAttribute("works", workService.getWorksForCorporateCustomerByProviderId(providerId));
        }
        model.addAttribute(providerId);
        return "appointments/selectService";
    }

    @GetMapping("/new/{providerId}/{workId}")
    public String selectDate(@PathVariable("workId") int workId, @PathVariable("providerId") int providerId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (workService.isWorkForCustomer(workId, currentUser.getId())) {
            model.addAttribute(providerId);
            model.addAttribute("workId", workId);
            return "appointments/selectDate";
        } else {
            return "redirect:/appointments/new";
        }

    }

    @GetMapping("/new/{providerId}/{workId}/{dateTime}")
    public String showNewAppointmentSummary(@PathVariable("workId") int workId, @PathVariable("providerId") int providerId, @PathVariable("dateTime") String start, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (appointmentService.isAvailable(workId, providerId, currentUser.getId(), LocalDateTime.parse(start))) {
            model.addAttribute("work", workService.getWorkById(workId));
            model.addAttribute("provider", userService.getProviderById(providerId).getLastName() + " " + userService.getProviderById(providerId).getFirstName());
            model.addAttribute(providerId);
            model.addAttribute("start", LocalDateTime.parse(start));
            model.addAttribute("end", LocalDateTime.parse(start).plusMinutes(workService.getWorkById(workId).getDuration()));
            return "appointments/newAppointmentSummary";
        } else {
            return "redirect:/appointments/new";
        }
    }

    @PostMapping("/new")
    public String bookAppointment(@RequestParam("workId") int workId, @RequestParam("providerId") int providerId, @RequestParam("start") String start, @AuthenticationPrincipal CustomUserDetails currentUser) {
        appointmentService.createNewAppointment(workId, providerId, currentUser.getId(), LocalDateTime.parse(start));
        return "redirect:/appointments/all";
    }

    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        appointmentService.cancelUserAppointmentById(appointmentId, currentUser.getId());
        return "redirect:/appointments/all";
    }


    @GetMapping("/download/{appointmentId}")
    public ResponseEntity<InputStreamResource> downloadAppointment(@PathVariable("appointmentId") int appointmentId, @AuthenticationPrincipal UserDetails currentUser) {
        try {
            // Lấy tệp PDF cho cuộc hẹn từ AppointmentService
            File appointmentPdf = appointmentService.generatePdfForAppointment(appointmentId);

            // Thiết lập các header cho response
            HttpHeaders respHeaders = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("application/pdf");
            respHeaders.setContentType(mediaType);
            respHeaders.setContentLength(appointmentPdf.length());
            respHeaders.setContentDispositionFormData("attachment", appointmentPdf.getName());

            // Tạo InputStreamResource từ tệp PDF
            InputStreamResource isr = new InputStreamResource(new FileInputStream(appointmentPdf));

            // Trả về ResponseEntity chứa InputStreamResource và các header đã thiết lập
            return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            // Xử lý nếu có lỗi khi tạo PDF
//            log.error("Lỗi khi tạo PDF để tải xuống, lỗi: {} ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }


    public static String formatDuration(Duration duration) {
        long s = duration.getSeconds();
        return String.format("%dh%02dm", s / 3600, (s % 3600) / 60);
    }

}
