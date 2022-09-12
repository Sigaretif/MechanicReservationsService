package com.zaioro.mechanicservice.model.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zaioro.mechanicservice.model.ReservationDate;
import com.zaioro.mechanicservice.model.car.Car;
import com.zaioro.mechanicservice.model.serviceType.ServiceType;
import com.zaioro.mechanicservice.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;

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

    @JsonFormat(pattern="dd/MM/yyyy h:mm a")
    private Date choosedDate;

    @ManyToOne
    @JoinColumn(name = "mechanic_id", referencedColumnName = "id")
    @NotFound
    private User mechanic;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @NotFound
    private User client;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @NotFound
    private Car car;

    @ManyToOne
    @JoinColumn(name = "service_type_id", referencedColumnName = "id")
    @NotFound
    private ServiceType serviceType;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    @NotFound
    private Set<ReservationDate> reservationDates;

    private String attachment;
    private String exactDescription;
    private String estimatedCost;
    private String status;
}
