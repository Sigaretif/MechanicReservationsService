package com.zaioro.mechanicservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zaioro.mechanicservice.model.car.Car;
import com.zaioro.mechanicservice.model.serviceType.ServiceType;
import com.zaioro.mechanicservice.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date choosedDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mechanic_id", referencedColumnName = "id")
    private User mechanic;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User client;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_type_id", referencedColumnName = "id")
    private ServiceType serviceType;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Set<ReservationDate> reservationDates;

    private String exactDescription;
    private String estimatedCost;
    private String status;
}
