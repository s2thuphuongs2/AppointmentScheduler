package com.example.appointmentscheduler.service.appointment;

import com.example.appointmentscheduler.dao.AppointmentRepository;
import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.entity.Work;
import com.example.appointmentscheduler.entity.WorkingPlan;
import com.example.appointmentscheduler.entity.user.customer.Customer;
import com.example.appointmentscheduler.entity.user.doctor.Doctor;
import com.example.appointmentscheduler.service.*;
import com.example.appointmentscheduler.service.impl.AppointmentServiceImpl;
import com.google.zxing.WriterException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private WorkService workService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private BarcodeService barcodeService;

    @Mock
    private AppointmentService appointmentService;
    @InjectMocks
    private AppointmentServiceImpl appointmentServiceTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private int customerId;
    private int doctorId;
    private int workId;

    private Appointment appointment;
    private Optional<Appointment> optionalAppointment;
    private List<Appointment> appointments;
    private int appointmentId;
    private Work work;
    private Doctor doctor;
    private Customer customer;

    private Long barcodeId;

    /**
     * Initializing objects for testing
     *
     */
    @Before
    public void initObjects() {
        // Initializing test data
        customerId = 1;
        doctorId = 2;
        workId = 3;
        work = new Work();
        work.setId(workId);
        work.setDuration(60);
        doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setWorkingPlan(WorkingPlan.generateDefaultWorkingPlan());
        customer = new Customer();
        customer.setId(customerId);
        appointment = new Appointment();
        appointmentId = 1;
        appointment.setId(appointmentId);
        optionalAppointment = Optional.of(appointment);
        appointments = new ArrayList<>();
        appointments.add(appointment);
        //TODO: Initialize barcodeId
        barcodeId = 123456789L;

    }
    /**
     * Test case for creating a new appointment when all conditions are met.
     *
     */
    @Test
    public void shouldBookAppointmentWhenAllConditionsMet() throws IOException, WriterException {
        LocalDateTime startOfNewAppointment = LocalDateTime.of(2019, 01, 01, 6, 0);

        //Mocking dependencies and behavior
        when(workService.isWorkForCustomer(workId, customerId)).thenReturn(true);
        when(workService.getWorkById(workId)).thenReturn(work);
        when(userService.getDoctorById(doctorId)).thenReturn(doctor);
        when(userService.getCustomerById(customerId)).thenReturn(customer);
        // TODO: Mocking the behavior of the barcodeService.genarateBarcodeImage method
//        when(barcodeService.genarateBarcodeImage(barcodeId)).thenReturn(new byte[]{/* Mocked byte array */});
        when(barcodeService.generateBarcodeImageAndSave(barcodeId)).thenReturn("src/main/resources/static/img/barcodes/123456789.png");
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        // Using ArgumentCaptor to capture the Appointment object that is passed to the appointmentRepository.save() method
        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        // TODO: Check if the appointmentService.createNewAppointment method does not throw any exception
        Work tmp = workService.getWorkById(workId);
        appointmentService.createNewAppointment(workId, doctorId, customerId, startOfNewAppointment);

        // Xác minh rằng phương thức lưu của cuộc hẹnRepository được gọi chính xác một lần với đối số đã ghi
        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotBookAppointmentWhenAppointmentStartIsNotWithinDoctorWorkingHours() {
        LocalDateTime startOfNewAppointment = LocalDateTime.of(2019, 01, 01, 5, 59);

        //Mocking dependencies and behavior
        when(workService.isWorkForCustomer(workId, customerId)).thenReturn(true);
        when(workService.getWorkById(workId)).thenReturn(work);
        when(userService.getDoctorById(doctorId)).thenReturn(doctor);

        // Using ArgumentCaptor to capture the Appointment object that is passed to the appointmentRepository.save() method
        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointmentService.createNewAppointment(workId, doctorId, customerId, startOfNewAppointment);
        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }

    /**
     * Test case for creating a new appointment when all conditions are met.
     */
    @Test(expected = RuntimeException.class)
    public void shouldNotBookNewAppointmentWhenCollidingWithDoctorAlreadyBookedAppointments() {
        LocalDateTime startOfNewAppointment = LocalDateTime.of(2019, 01, 01, 6, 0);

        // Mocking dependencies and behavior
        Appointment existingAppointment = new Appointment();
        LocalDateTime startOfExistingAppointment = LocalDateTime.of(2019, 01, 01, 6, 0);
        LocalDateTime endOfExistingAppointment = LocalDateTime.of(2019, 01, 01, 7, 0);
        existingAppointment.setStart(startOfExistingAppointment);
        existingAppointment.setEnd(endOfExistingAppointment);
        List<Appointment> doctorBookedAppointments = new ArrayList<>();
        doctorBookedAppointments.add(existingAppointment);

        when(workService.isWorkForCustomer(workId, customerId)).thenReturn(true);
        when(appointmentRepository.findByDoctorIdWithStartInPeroid(doctorId, startOfNewAppointment.toLocalDate().atStartOfDay(), startOfNewAppointment.toLocalDate().atStartOfDay().plusDays(1))).thenReturn(doctorBookedAppointments);
        when(workService.getWorkById(workId)).thenReturn(work);
        when(userService.getDoctorById(doctorId)).thenReturn(doctor);

        // Sử dụng ArgumentCaptor để nắm bắt đối số được truyền vào phương thức save
        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointmentService.createNewAppointment(workId, doctorId, customerId, startOfNewAppointment);

        //
        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }

    /**
     * Test case for creating a new appointment when all conditions are met.
     * Kiểm tra việc không tạo cuộc hẹn mới khi xung đột với các cuộc hẹn đã được đặt trước của khách hàng.
     */
    @Test(expected = RuntimeException.class)
    public void shouldNotBookNewAppointmentWhenCollidingWithCustomerAlreadyBookedAppointments() {
        LocalDateTime startOfNewAppointment = LocalDateTime.of(2019, 01, 01, 6, 0);

        // Mocking dependencies and behavior
        Appointment existingAppointment = new Appointment();
        LocalDateTime startOfExistingAppointment = LocalDateTime.of(2019, 01, 01, 6, 0);
        LocalDateTime endOfExistingAppointment = LocalDateTime.of(2019, 01, 01, 7, 0);
        existingAppointment.setStart(startOfExistingAppointment);
        existingAppointment.setEnd(endOfExistingAppointment);
        List<Appointment> customerBookedAppointments = new ArrayList<>();
        customerBookedAppointments.add(existingAppointment);

        when(workService.isWorkForCustomer(workId, customerId)).thenReturn(true);
        when(appointmentRepository.findByCustomerIdWithStartInPeroid(customerId, startOfNewAppointment.toLocalDate().atStartOfDay(), startOfNewAppointment.toLocalDate().atStartOfDay().plusDays(1))).thenReturn(customerBookedAppointments);
        when(workService.getWorkById(workId)).thenReturn(work);
        when(userService.getDoctorById(doctorId)).thenReturn(doctor);

        // Sử dụng ArgumentCaptor để nắm bắt đối số được truyền vào phương thức save
        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointmentService.createNewAppointment(workId, doctorId, customerId, startOfNewAppointment);

        // Xác minh rằng phương thức lưu của cuộc hẹnRepository được gọi chính xác một lần với đối số đã ghi
        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }

    /**
     *
     */
    @Test
    public void shouldFindAppointmentById() {
        when(appointmentRepository.findById(1)).thenReturn(optionalAppointment);
        Assert.assertEquals(optionalAppointment.get().getId(), appointmentService.getAppointmentByIdWithAuthorization(1).getId());
        verify(appointmentRepository, times(1)).findById(1);
    }

    @Test
    public void shouldFindAllAppointments() {
        when(appointmentRepository.findAll()).thenReturn(appointments);
        assertEquals(appointments, appointmentService.getAllAppointments());
        verify(appointmentRepository).findAll();
    }

    @Test
    public void shouldDeleteAppointmentById() {
        appointmentService.deleteAppointmentById(1);
        verify(appointmentRepository).deleteById(1);
    }


}
