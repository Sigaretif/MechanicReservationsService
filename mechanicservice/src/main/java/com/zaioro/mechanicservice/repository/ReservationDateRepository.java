package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.ReservationDate;
import com.zaioro.mechanicservice.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationDateRepository extends JpaRepository<ReservationDate, Integer> {
    List<ReservationDate> findAllByReservation(Reservation reservation);
}
