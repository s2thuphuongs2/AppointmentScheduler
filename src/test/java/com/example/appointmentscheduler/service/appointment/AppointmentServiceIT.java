package com.example.appointmentscheduler.service.appointment;

import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.entity.AppointmentStatus;
import com.example.appointmentscheduler.service.AppointmentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
public class AppointmentServiceIT {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Test case for {@link AppointmentService#getAllAppointments()}
     *
     */
    @Test
    @Transactional
    @WithUserDetails("admin")
    public void shouldSaveNewRetailCustomer() {
        // Creating a new appointment with  workId= 1, providerId= 3, customerId = 3, time = 2020-02-09 12:00:00
        appointmentService.createNewAppointment(1, 2, 3, LocalDateTime.of(2020, 02, 9, 12, 0, 0));
        // Retrieving all appointments
        List<Appointment> appointmentByProviderId = appointmentService.getAllAppointments();
        // Asserting that the size of the appointment list is 2
        assertThat(appointmentByProviderId).hasSize(2);
        // Asserting that the status of the first appointment in the list is "SCHEDULED"
        Assert.assertEquals(AppointmentStatus.SCHEDULED, appointmentByProviderId.get(0).getStatus());

    }

}
