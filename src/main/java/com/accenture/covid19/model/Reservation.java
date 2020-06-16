package com.accenture.covid19.model;

import com.accenture.covid19.dto.User;
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

    public Reservation() {
    }

    public Reservation(User user, Boolean onWaitingList) {
        this.userId = user.getUserId();
        this.bookedDate = user.getDate();
        this.onWaitingList = onWaitingList;
    }

    public Reservation(Integer id, LocalDateTime checkIn, LocalDateTime checkOut, Boolean inactive) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.inactive = inactive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(LocalDate bookedDate) {
        this.bookedDate = bookedDate;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Boolean getOnWaitingList() {
        return onWaitingList;
    }

    public void setOnWaitingList(Boolean onWaitingList) {
        this.onWaitingList = onWaitingList;
    }
}
