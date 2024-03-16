package com.example.appointmentscheduler.service;

import com.example.appointmentscheduler.entity.Work;

import java.util.List;

public interface WorkService {
    void createNewWork(Work work);

    Work getWorkById(int workId);

    List<Work> getAllWorks();

    List<Work> getWorksByDoctorId(int doctorId);

    List<Work> getRetailCustomerWorks();

    List<Work> getCorporateCustomerWorks();

    List<Work> getWorksForRetailCustomerByDoctorId(int doctorId);

    List<Work> getWorksForCorporateCustomerByDoctorId(int doctorId);

    void updateWork(Work work);

    void deleteWorkById(int workId);

    boolean isWorkForCustomer(int workId, int customerId);
}
