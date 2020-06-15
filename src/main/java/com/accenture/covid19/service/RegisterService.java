package com.accenture.covid19.service;

import com.accenture.covid19.dto.ResponseDTO;
import com.accenture.covid19.dto.UserDTO;
import com.accenture.covid19.model.Reservation;

import java.time.LocalDate;

public interface RegisterService {

    ResponseDTO save(UserDTO userDTO);

    default void checkUserHasBook(UserDTO userDTO) {
        checkUserHasBook(userDTO.getUserId(), userDTO.getDate());
    }

    default void checkUserHasBook(String userId) {
        checkUserHasBook(userId, LocalDate.now());
    }

    void checkUserHasBook(String userId, LocalDate date);


    default Integer findWaitingListNumber(String userId) {
        return findWaitingListNumber(userId, LocalDate.now());
    }

    Integer findWaitingListNumber(String userId, LocalDate date);

    default Integer findWaitingListNumber(UserDTO userDTO) {
        return findWaitingListNumber(userDTO.getUserId(), userDTO.getDate());
    }

    default Reservation getBookByUserAndDate(String userId) {
        return getBookByUserAndDate(LocalDate.now(), userId);
    }

    default Reservation getBookByUserAndDate(UserDTO userDTO) {
        return getBookByUserAndDate(userDTO.getDate(), userDTO.getUserId());
    }

    Reservation getBookByUserAndDate(LocalDate date, String userId);

}
