package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.car.Car;
import com.zaioro.mechanicservice.model.reservation.Reservation;
import com.zaioro.mechanicservice.model.serviceType.ServiceType;
import com.zaioro.mechanicservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByClient(User client);
    List<Reservation> findAllByClientAndCarAndServiceTypeAndExactDescription(User client, Car car, ServiceType serviceType, String exactDescription);

    @Modifying
    @Query("delete from Reservation r where r.id = :id")
    void deleteReservationUsingId(@Param("id") Integer id);
}
