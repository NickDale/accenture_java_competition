package com.accenture.covid19.repository;

import com.accenture.covid19.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.bookedDate =:date AND r.userId =:user AND r.checkIn IS NULL")
    int existReservationByUserAndDate(@Param("user") String userId, @Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.bookedDate =:date AND r.onWaitingList = TRUE ")
    List<Reservation> listWaitingList(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.bookedDate =:date AND r.onWaitingList = FALSE")
    List<Reservation> listReservation(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.bookedDate =:date AND r.userId =:user AND r.onWaitingList = FALSE AND r.checkOut is null")
    Optional<Reservation> findReservationByUserIdAndBookedDate(@Param("date") LocalDate date, @Param("user") String userId);

    @Query("SELECT r FROM Reservation r WHERE r.bookedDate =:date AND r.userId =:user AND r.checkIn is null")
    Optional<Reservation> findByUserIdAndBookedDate(@Param("date") LocalDate date, @Param("user") String userId);

    /**
     * Get number of the people whose are int the office or booked successfuly
     *
     * @param date
     * @return
     */
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.onWaitingList = FALSE AND r.checkIn IS NOT NULL AND r.checkOut IS NULL")
    BigDecimal getNumberOfPeopleInOffice(@Param("date") LocalDate date);

    /**
     * Get number of the people whose are int the office or booked successfuly
     *
     * @param date
     * @return
     */
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.onWaitingList = FALSE AND r.checkOut IS NULL")
    BigDecimal getNumberOfPeopleBookedInOffice(@Param("date") LocalDate date);

    @Query(value = "SELECT * FROM Reservation WHERE user_id =?1 AND on_waiting_list = FALSE AND check_in IS NOT NULL " +
            "AND check_out IS NULL ORDER BY check_in DESC  LIMIT 1", nativeQuery = true)
    Reservation findLastCheckInByUser(String userId);
}
