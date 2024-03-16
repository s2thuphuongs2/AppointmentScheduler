package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.entity.WorkingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkingPlanRepository extends JpaRepository<WorkingPlan, Integer> {
    @Query("select w from WorkingPlan w where w.doctor.id = :doctorId")
    WorkingPlan getWorkingPlanByDoctorId(@Param("doctorId") int doctorId);
}
