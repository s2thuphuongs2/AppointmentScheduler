package com.example.appointmentscheduler.dao.user.doctor;

import com.example.appointmentscheduler.entity.Work;
import com.example.appointmentscheduler.dao.user.CommonUserRepository;
import com.example.appointmentscheduler.entity.user.doctor.Doctor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends CommonUserRepository<Doctor> {

    List<Doctor> findByWorks(Work work);

    @Query("select distinct p from Doctor p inner join p.works w where w.targetCustomer = 'retail'")
    List<Doctor> findAllWithRetailWorks();

    @Query("select distinct p from Doctor p inner join p.works w where w.targetCustomer = 'corporate'")
    List<Doctor> findAllWithCorporateWorks();
}
