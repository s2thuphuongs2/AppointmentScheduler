package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.entity.WorkingPlan;
import com.example.appointmentscheduler.model.ChangePasswordForm;
import com.example.appointmentscheduler.model.TimePeroid;
import com.example.appointmentscheduler.model.UserForm;
import com.example.appointmentscheduler.security.CustomUserDetails;
import com.example.appointmentscheduler.service.AppointmentService;
import com.example.appointmentscheduler.service.UserService;
import com.example.appointmentscheduler.service.WorkService;
import com.example.appointmentscheduler.service.WorkingPlanService;
import com.example.appointmentscheduler.validation.groups.CreateDoctor;
import com.example.appointmentscheduler.validation.groups.CreateUser;
import com.example.appointmentscheduler.validation.groups.UpdateDoctor;
import com.example.appointmentscheduler.validation.groups.UpdateUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final UserService userService;
    private final WorkService workService;
    private final WorkingPlanService workingPlanService;
    private final AppointmentService appointmentService;

    public DoctorController(UserService userService, WorkService workService, WorkingPlanService workingPlanService, AppointmentService appointmentService) {
        this.userService = userService;
        this.workService = workService;
        this.workingPlanService = workingPlanService;
        this.appointmentService = appointmentService;
    }


    @GetMapping("/all")
    public String showAllDoctors(Model model) {
        model.addAttribute("doctors", userService.getAllDoctors());
        return "users/listDoctors";
    }

    @GetMapping("/{id}")
    public String showDoctorDetails(@PathVariable("id") int doctorId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.getId() == doctorId || currentUser.hasRole("ROLE_ADMIN")) {
            if (!model.containsAttribute("user")) {
                model.addAttribute("user", new UserForm(userService.getDoctorById(doctorId)));
            }
            if (!model.containsAttribute("passwordChange")) {
                model.addAttribute("passwordChange", new ChangePasswordForm(doctorId));
            }
            model.addAttribute("account_type", "doctor");
            model.addAttribute("formActionProfile", "/doctors/update/profile");
            model.addAttribute("formActionPassword", "/doctors/update/password");
            model.addAttribute("allWorks", workService.getAllWorks());
            model.addAttribute("numberOfScheduledAppointments", appointmentService.getNumberOfScheduledAppointmentsForUser(doctorId));
            model.addAttribute("numberOfCanceledAppointments", appointmentService.getNumberOfCanceledAppointmentsForUser(doctorId));
            return "users/updateUserForm";

        } else {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
    }

    @PostMapping("/update/profile")
    public String processDoctorUpdate(@Validated({UpdateUser.class, UpdateDoctor.class}) @ModelAttribute("user") UserForm userUpdateData, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userUpdateData);
            return "redirect:/doctors/" + userUpdateData.getId();
        }
        userService.updateDoctorProfile(userUpdateData);
        return "redirect:/doctors/" + userUpdateData.getId();
    }

    @GetMapping("/new")
    public String showDoctorRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) model.addAttribute("user", new UserForm());
        model.addAttribute("account_type", "doctor");
        model.addAttribute("registerAction", "/doctors/new");
        model.addAttribute("allWorks", workService.getAllWorks());
        return "users/createUserForm";
    }

    @PostMapping("/new")
    public String processDoctorRegistrationForm(@Validated({CreateUser.class, CreateDoctor.class}) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userForm);
            return "redirect:/doctors/new";
        }
        userService.saveNewDoctor(userForm);
        return "redirect:/doctors/all";
    }

    @PostMapping("/delete")
    public String processDeleteDoctorRequest(@RequestParam("doctorId") int doctorId) {
        userService.deleteUserById(doctorId);
        return "redirect:/doctors/all";
    }

    @GetMapping("/availability")
    public String showDoctorAvailability(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("plan", workingPlanService.getWorkingPlanByDoctorId(currentUser.getId()));
        model.addAttribute("breakModel", new TimePeroid());
        return "users/showOrUpdateDoctorAvailability";
    }

    @PostMapping("/availability")
    public String processDoctorWorkingPlanUpdate(@ModelAttribute("plan") WorkingPlan plan) {
        workingPlanService.updateWorkingPlan(plan);
        return "redirect:/doctors/availability";
    }

    @PostMapping("/availability/breakes/add")
    public String processDoctorAddBreak(@ModelAttribute("breakModel") TimePeroid breakToAdd, @RequestParam("planId") int planId, @RequestParam("dayOfWeek") String dayOfWeek) {
        workingPlanService.addBreakToWorkingPlan(breakToAdd, planId, dayOfWeek);
        return "redirect:/doctors/availability";
    }

    @PostMapping("/availability/breakes/delete")
    public String processDoctorDeleteBreak(@ModelAttribute("breakModel") TimePeroid breakToDelete, @RequestParam("planId") int planId, @RequestParam("dayOfWeek") String dayOfWeek) {
        workingPlanService.deleteBreakFromWorkingPlan(breakToDelete, planId, dayOfWeek);
        return "redirect:/doctors/availability";
    }

    @PostMapping("/update/password")
    public String processDoctorPasswordUpdate(@Valid @ModelAttribute("passwordChange") ChangePasswordForm passwordChange, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordChange", bindingResult);
            redirectAttributes.addFlashAttribute("passwordChange", passwordChange);
            return "redirect:/doctors/" + passwordChange.getId();
        }
        userService.updateUserPassword(passwordChange);
        return "redirect:/doctors/" + passwordChange.getId();
    }


}
