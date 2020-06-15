package com.accenture.covid19.model;

import com.accenture.covid19.dto.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Where(clause = "inactive = false")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "booked_date", nullable = false)
    private LocalDate bookedDate;
    @Column(name = "check_in")
    private LocalDateTime checkIn;
    @Column(name = "check_out")
    private LocalDateTime checkOut;
    @Column(name = "inactive")
    private Boolean inactive = Boolean.FALSE;
    @Column(name = "on_waiting_list")
    private Boolean onWaitingList = Boolean.FALSE;

    public Reservation(User user, Boolean onWaitingList) {
        this.userId = user.getUserId();
        this.bookedDate = user.getDate();
        this.onWaitingList = onWaitingList;
    }

}
