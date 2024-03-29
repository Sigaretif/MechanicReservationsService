package com.zaioro.mechanicservice.model.car;

import com.zaioro.mechanicservice.model.reservation.Reservation;
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
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "car", schema = "public")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private String body;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotFound
    private User user;
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @NotFound
    private Set<Reservation> reservations;
}
