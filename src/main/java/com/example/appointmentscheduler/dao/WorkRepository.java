package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Integer> {
    @Query("select w from Work w inner join w.doctors p where p.id in :doctorId")
    List<Work> findByDoctorId(@Param("doctorId") int doctorId);

    @Query("select w from Work w where w.targetCustomer = :target ")
    List<Work> findByTargetCustomer(@Param("target") String targetCustomer);

    @Query("select w from Work w inner join w.doctors p where p.id in :doctorId and w.targetCustomer = :target ")
    List<Work> findByTargetCustomerAndDoctorId(@Param("target") String targetCustomer, @Param("doctorId") int doctorId);
}
