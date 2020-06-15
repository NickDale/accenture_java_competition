package com.accenture.covid19.service;

import com.accenture.covid19.dto.SimpleStringResponse;
import com.accenture.covid19.dto.User;
import com.accenture.covid19.model.Reservation;

import java.time.LocalDate;

public interface RegisterService {

    SimpleStringResponse save(User user);

    default void checkUserHasBook(User user) {
        checkUserHasBook(user.getUserId(), user.getDate());
    }

    default void checkUserHasBook(String userId) {
        checkUserHasBook(userId, LocalDate.now());
    }

    void checkUserHasBook(String userId, LocalDate date);


    default Integer findWaitingListNumber(String userId) {
        return findWaitingListNumber(userId, LocalDate.now());
    }

    Integer findWaitingListNumber(String userId, LocalDate date);

    default Integer findWaitingListNumber(User user) {
        return findWaitingListNumber(user.getUserId(), user.getDate());
    }

    default Reservation getBookByUserAndDate(String userId) {
        return getBookByUserAndDate(LocalDate.now(), userId);
    }

    default Reservation getBookByUserAndDate(User user) {
        return getBookByUserAndDate(user.getDate(), user.getUserId());
    }

    Reservation getBookByUserAndDate(LocalDate date, String userId);

}
