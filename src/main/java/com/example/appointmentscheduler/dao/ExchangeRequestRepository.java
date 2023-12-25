package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.entity.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer> {
}
