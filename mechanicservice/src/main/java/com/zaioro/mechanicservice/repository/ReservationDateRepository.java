package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.ReservationDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDateRepository extends JpaRepository<ReservationDate, Integer> {
}
