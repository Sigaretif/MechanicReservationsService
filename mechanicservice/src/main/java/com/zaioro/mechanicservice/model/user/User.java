package com.zaioro.mechanicservice.model.user;

import com.zaioro.mechanicservice.model.Address;
import com.zaioro.mechanicservice.model.Reservation;
import com.zaioro.mechanicservice.model.car.Car;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "user", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    private String status;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Car> cars;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Reservation> clientReservations;
    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL)
    private Set<Reservation> mechanicReservations;
}
