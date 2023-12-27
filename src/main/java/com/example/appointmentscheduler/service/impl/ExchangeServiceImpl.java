package com.example.appointmentscheduler.service.impl;

import com.example.appointmentscheduler.service.NotificationService;
import com.example.appointmentscheduler.dao.AppointmentRepository;
import com.example.appointmentscheduler.dao.ExchangeRequestRepository;
import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.entity.AppointmentStatus;
import com.example.appointmentscheduler.entity.ExchangeRequest;
import com.example.appointmentscheduler.entity.ExchangeStatus;
import com.example.appointmentscheduler.entity.user.customer.Customer;
import com.example.appointmentscheduler.service.ExchangeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;
    private final ExchangeRequestRepository exchangeRequestRepository;

    public ExchangeServiceImpl(AppointmentRepository appointmentRepository, NotificationService notificationService, ExchangeRequestRepository exchangeRequestRepository) {
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
        this.exchangeRequestRepository = exchangeRequestRepository;
    }

    @Override
    public boolean checkIfEligibleForExchange(int userId, int appointmentId) {
        Appointment appointment = appointmentRepository.getOne(appointmentId);
        return appointment.getStart().minusHours(24).isAfter(LocalDateTime.now()) && appointment.getStatus().equals(AppointmentStatus.SCHEDULED) && appointment.getCustomer().getId() == userId;
    }

    @Override
    public List<Appointment> getEligibleAppointmentsForExchange(int appointmentId) {
        Appointment appointmentToExchange = appointmentRepository.getOne(appointmentId);
        return appointmentRepository.getEligibleAppointmentsForExchange(LocalDateTime.now().plusHours(24), appointmentToExchange.getCustomer().getId(), appointmentToExchange.getProvider().getId(), appointmentToExchange.getWork().getId());
    }
    /**
     * Kiểm tra xem trao đổi có thể thực hiện được không
     * @param oldAppointmentId id of the appointment to be exchanged
     * @param newAppointmentId id of the appointment to be exchanged for
     * @param userId id of the user
     * @return true if exchange is possible
     */
    @Override
    public boolean checkIfExchangeIsPossible(int oldAppointmentId, int newAppointmentId, int userId) {
        // Lấy thông tin cuộc hẹn cũ và cuộc hẹn mới từ cơ sở dữ liệu
        Appointment oldAppointment = appointmentRepository.getOne(oldAppointmentId);
        Appointment newAppointment = appointmentRepository.getOne(newAppointmentId);
        // check if old appointment is scheduled and is within 24 hours of now
        if (oldAppointment.getCustomer().getId() == userId) {
            return oldAppointment.getWork().getId().equals(newAppointment.getWork().getId()) // Kiểm tra xem cuộc hẹn cũ và cuộc hẹn mới có cùng công việc không
                    && oldAppointment.getProvider().getId().equals(newAppointment.getProvider().getId()) // Kiểm tra xem cuộc hẹn cũ và cuộc hẹn mới có cùng nhà cung cấp không
                    && oldAppointment.getStart().minusHours(24).isAfter(LocalDateTime.now())// Kiểm tra xem cuộc hẹn cũ có trong vòng 24 giờ không
                    && newAppointment.getStart().minusHours(24).isAfter(LocalDateTime.now());// Kiểm tra xem cuộc hẹn mới có trong vòng 24 giờ không
        } else {
            // Nếu người dùng không có quyền truy cập cuộc hẹn cũ, ném một ngoại lệ Unauthorized
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }

    }
    /**
     * Chấp nhận yêu cầu trao đổi
     * @param exchangeId id of the exchange request
     * @param userId id of the user
     * @return true if successful
     */
    @Override
    public boolean acceptExchange(int exchangeId, int userId) {
        // get exchange request by id
        ExchangeRequest exchangeRequest = exchangeRequestRepository.getOne(exchangeId);
        // get requestor appointment and set status to scheduled
        Appointment requestor = exchangeRequest.getRequestor();
        Appointment requested = exchangeRequest.getRequested();
        // swap customers
        Customer tempCustomer = requestor.getCustomer();
        // set status of old appointment to scheduled
        requestor.setStatus(AppointmentStatus.SCHEDULED);
        exchangeRequest.setStatus(ExchangeStatus.ACCEPTED);
        // swap customers
        requestor.setCustomer(requested.getCustomer());
        requested.setCustomer(tempCustomer);
        exchangeRequestRepository.save(exchangeRequest);
        appointmentRepository.save(requested);
        appointmentRepository.save(requestor);
        // send notification to requestor
        notificationService.newExchangeAcceptedNotification(exchangeRequest, true);
        return true;
    }
    /**
     * Từ chối yêu cầu trao đổi
     * @param exchangeId id of the exchange request
     * @param userId id of the user
     * @return true if successful
     */
    @Override
    public boolean rejectExchange(int exchangeId, int userId) {
        // get exchange request by id
        ExchangeRequest exchangeRequest = exchangeRequestRepository.getOne(exchangeId);
        // get requestor appointment and set status to scheduled
        Appointment requestor = exchangeRequest.getRequestor();
        exchangeRequest.setStatus(ExchangeStatus.REJECTED);
        requestor.setStatus(AppointmentStatus.SCHEDULED);
        exchangeRequestRepository.save(exchangeRequest);
        appointmentRepository.save(requestor);
        notificationService.newExchangeRejectedNotification(exchangeRequest, true);
        return true;
    }
    /**
     * Yêu cầu trao đổi
     * @param oldAppointmentId id of the appointment to be exchanged
     * @param newAppointmentId id of the appointment to be exchanged for
     * @param userId id of the user
     * @return true if successful
     */
    @Override
    public boolean requestExchange(int oldAppointmentId, int newAppointmentId, int userId) {
        if (checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, userId)) {
            Appointment oldAppointment = appointmentRepository.getOne(oldAppointmentId);
            Appointment newAppointment = appointmentRepository.getOne(newAppointmentId);
            // set status of old appointment to exchange requested
            oldAppointment.setStatus(AppointmentStatus.EXCHANGE_REQUESTED);
            appointmentRepository.save(oldAppointment);
            ExchangeRequest exchangeRequest = new ExchangeRequest(oldAppointment, newAppointment, ExchangeStatus.PENDING);
            // save exchange request
            exchangeRequestRepository.save(exchangeRequest);
            // send notification to provider
            notificationService.newExchangeRequestedNotification(oldAppointment, newAppointment, true);
            return true;
        }
        return false;
    }
}
