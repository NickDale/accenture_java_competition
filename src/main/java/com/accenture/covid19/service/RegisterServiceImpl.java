package com.accenture.covid19.service;

import com.accenture.covid19.dto.SimpleStringResponse;
import com.accenture.covid19.dto.User;
import com.accenture.covid19.exception.AlreadyBookedException;
import com.accenture.covid19.exception.InvalidDateException;
import com.accenture.covid19.model.Reservation;
import com.accenture.covid19.repository.ReservationRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
public class RegisterServiceImpl implements RegisterService {

    public static final String ERROR_MSG = "error_msg";

    @Value("${max_capacity:250}")
    private String maxCapacity;

    @Value("${actual_capacity_percent:100}")
    private String actualCapacityPercent;

    @Autowired
    private ReservationRepository reservationRepository;

    public SimpleStringResponse save(User user) {
        checkUserHasBook(user);
        LocalDate date = user.getDate();
        checkDateIsValid(date);

        BigDecimal currentBook = reservationRepository.getNumberOfPeopleBookedInOffice(date);
        BigDecimal freeCapacity = getActualCapacity().subtract(currentBook);

        Reservation reservation = new Reservation(user, freeCapacity.signum() < 1);
        reservationRepository.save(reservation);
        Integer number = findWaitingListNumber(user);
        return new SimpleStringResponse(number != 0 ? "You are the " + number + ". on the waiting-list" : "Successfully registered ");
    }

    private BigDecimal getActualCapacity() {
        return new BigDecimal(maxCapacity).movePointLeft(2).multiply(new BigDecimal(actualCapacityPercent));
    }

    private void checkDateIsValid(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("You can not book in the past");
        }
    }

    public void checkUserHasBook(String userId, LocalDate date) {
        int userBooksNumber = reservationRepository.existReservationByUserAndDate(userId, date);
        if (userBooksNumber > 0) {
            throw new AlreadyBookedException("You already booked for this day");
        }
    }

    /**
     * User position in the waiting-list
     *
     * @param userId
     * @param date
     * @return Integer
     */
    @Override
    public Integer findWaitingListNumber(String userId, LocalDate date) {
        var booksByDate = getIdOrderedList(date);
        int count = 0;

        for (Reservation reservation : booksByDate) {
            count++;
            if (reservation.getUserId().equalsIgnoreCase(userId)) {
                return count;
            }
        }
        return count;
    }

    /**
     * List the reservations by date and order it by reservation id.
     *
     * @param date
     * @return List<Reservation>
     */
    private List<Reservation> getIdOrderedList(LocalDate date) {
        return reservationRepository.listWaitingList(date)
                .stream().sorted(Comparator.comparing(Reservation::getId))
                .collect(toList());
    }

    /**
     * Give back the user position on the waiting-list.
     *
     * @param userId
     * @return ResponseDTO.class
     * @throws AlreadyBookedException
     */
    public SimpleStringResponse getStatus(String userId) {
        int userBooksNumber = reservationRepository.existReservationByUserAndDate(userId, LocalDate.now());
        if (userBooksNumber == 0) {
            throw new AlreadyBookedException("You have not booked for this day yet");
        }

        Integer waitingListNumber = findWaitingListNumber(userId);
        return new SimpleStringResponse(waitingListNumber != 0 ?
                "You are the " + waitingListNumber + ". on the waiting-list"
                : "You are not on the waiting list ");
    }


    public Boolean checkIn(String userId) {
        Reservation reservation = getBookByUserAndDate(userId);
        if (isNull(reservation)) {
            throw new AlreadyBookedException("You have not booked for this day yet");
        }

        LocalDate now = LocalDate.now();
        BigDecimal numberOfPeopleInOffice = reservationRepository.getNumberOfPeopleInOffice(now);
        BigDecimal freeCapacityInOffice = getActualCapacity().subtract(numberOfPeopleInOffice);
        if (freeCapacityInOffice.signum() < 1) {
            return false; // office is full
        }

        List<Reservation> reservations = reservationRepository.listReservation(now);
        reservations.sort(Comparator.comparing(Reservation::getId));

        int count = 0;
        for (Reservation b : reservations) {
            if (b.getUserId().equalsIgnoreCase(userId)) {
                break;
            }
            if (nonNull(b.getCheckIn())) {
                count++;
                // return count;
            }
        }
        if (count >= freeCapacityInOffice.intValue()) {
            return false;
        }

        reservation.setCheckIn(LocalDateTime.now());
        reservationRepository.save(reservation);
        return true;
    }


    public void checkOut(String userId) {
        Reservation lastCheckInByUser = getBookByUserAndDate(userId);
        if (isNull(lastCheckInByUser)) {
            lastCheckInByUser = findLastCheckInByUser(userId);
            if (isNull(lastCheckInByUser)) {
                //it should be never happened
                throw new EntityNotFoundException("Can't find the check in");
            }
        }

        lastCheckInByUser.setCheckOut(LocalDateTime.now());
        reservationRepository.save(lastCheckInByUser);
        upDateWaitingList();
    }

    public Reservation findLastCheckInByUser(String userId) {
        return reservationRepository.findLastCheckInByUser(userId);
    }

    private void upDateWaitingList() {
        List<Reservation> reservations = getIdOrderedList(LocalDate.now());
        if (CollectionUtils.isNotEmpty(reservations)) {
            Reservation firstOnWaitingList = reservations.get(0);
            firstOnWaitingList.setOnWaitingList(Boolean.FALSE);
            reservationRepository.save(firstOnWaitingList);
        }
    }

    public Reservation getBookByUserAndDate(LocalDate date, String userId) {
        return reservationRepository.findReservationByUserIdAndBookedDate(date, userId).orElse(null);
    }

    public void deleteBook(User user) {
        Reservation reservationByUserAndDate = reservationRepository.findByUserIdAndBookedDate(user.getDate(), user.getUserId()).orElse(null);
        if (isNull(reservationByUserAndDate)) {
            throw new EntityNotFoundException("You have not booked on this day");
        }
        reservationByUserAndDate.setInactive(true);
        reservationRepository.save(reservationByUserAndDate);

        LocalDate now = LocalDate.now();
        if (user.getDate().isEqual(now)) {
            BigDecimal numberOfPeopleInOffice = reservationRepository.getNumberOfPeopleBookedInOffice(now);
            BigDecimal freeCapacityInOffice = getActualCapacity().subtract(numberOfPeopleInOffice);
            if (freeCapacityInOffice.signum() > 0) {
                upDateWaitingList();
            }
        }

    }

}
