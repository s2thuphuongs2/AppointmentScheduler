package com.example.appointmentscheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exchange")
public class ExchangeController {
//
//    private final ExchangeService exchangeService;
//    private final AppointmentService appointmentService;
//
//    public ExchangeController(ExchangeService exchangeService, AppointmentService appointmentService) {
//        this.exchangeService = exchangeService;
//        this.appointmentService = appointmentService;
//    }
//    /**
//     * Hiển thị danh sách các cuộc hẹn có thể trao đổi cho một cuộc hẹn cũ
//     * @param model
//     * @return list of all appointments that are eligible for exchange
//     */
//    @GetMapping("/{oldAppointmentId}")
//    public String showEligibleAppointmentsToExchange(@PathVariable("oldAppointmentId") int oldAppointmentId, Model model) {
//        List<Appointment> eligibleAppointments = exchangeService.getEligibleAppointmentsForExchange(oldAppointmentId);
//        model.addAttribute("appointmentId", oldAppointmentId);
//        model.addAttribute("numberOfEligibleAppointments", eligibleAppointments.size());
//        model.addAttribute("eligibleAppointments", eligibleAppointments);
//        return "exchange/listProposals";
//    }
//    /**
//     * Hiển thị màn hình tóm tắt trao đổi giữa một cuộc hẹn cũ và một cuộc hẹn mới
//     * @param oldAppointmentId id of the appointment to be exchanged
//     * @param newAppointmentId id of the appointment to be exchanged for
//     * @param model
//     * @return exchange summary screen
//     */
//    @GetMapping("/{oldAppointmentId}/{newAppointmentId}")
//    public String showExchangeSummaryScreen(@PathVariable("oldAppointmentId") int oldAppointmentId, @PathVariable("newAppointmentId") int newAppointmentId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
//        // Kiểm tra xem trao đổi có thể thực hiện được không
//        if (exchangeService.checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, currentUser.getId())) {
//            model.addAttribute("oldAppointment", appointmentService.getAppointmentByIdWithAuthorization(oldAppointmentId));
//            model.addAttribute("newAppointment", appointmentService.getAppointmentById(newAppointmentId));
//        } else {
//            return "redirect:/appointments/all";
//        }
//
//        return "exchange/exchangeSummary";
//    }
//    /**
//     * Xử lý yêu cầu trao đổi
//     * @param oldAppointmentId id of the appointment to be exchanged
//     * @param newAppointmentId id of the appointment to be exchanged for
//     * @param model
//     * @return exchange request confirmation screen
//     */
//    @PostMapping()
//    public String processExchangeRequest(@RequestParam("oldAppointmentId") int oldAppointmentId, @RequestParam("newAppointmentId") int newAppointmentId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
//        boolean result = exchangeService.requestExchange(oldAppointmentId, newAppointmentId, currentUser.getId());
//        if (result) {
//            model.addAttribute("message", "Exchange request sucsessfully sent!");
//        } else {
//            model.addAttribute("message", "Error! Exchange not sent!");
//        }
//        return "exchange/requestConfirmation";
//    }
//
//    /**
//     * Xử lý chấp nhập yêu cầu trao đổi
//     * @param exchangeId
//     * @param model
//     * @param currentUser
//     * @return redirect to the list of all appointments
//     */
//    @PostMapping("/accept")
//    public String processExchangeAcceptation(@RequestParam("exchangeId") int exchangeId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
//        exchangeService.acceptExchange(exchangeId, currentUser.getId());
//        return "redirect:/appointments/all";
//    }
//
//    /**
//     * Xử lý từ chối yêu cầu trao đổi
//     * @param exchangeId
//     * @param model
//     * @param currentUser
//     * @return redirect to the list of all appointments
//     */
//    @PostMapping("/reject")
//    public String processExchangeRejection(@RequestParam("exchangeId") int exchangeId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
//        exchangeService.rejectExchange(exchangeId, currentUser.getId());
//        return "redirect:/appointments/all";
//    }
}
