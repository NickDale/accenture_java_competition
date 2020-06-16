package com.accenture.covid19.repository;

import com.accenture.covid19.dto.User;
import com.accenture.covid19.model.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ReservationRepository repository;

    @Test
    public void test() throws Exception {
        LocalDate date = LocalDate.of(2020, 6, 15);
        String userId = "id";
        User user = new User(userId, date);
        Reservation reservation = new Reservation(user, false);
        reservation.setCheckIn(LocalDateTime.now());
        LocalDate date2 = LocalDate.of(2020, 7, 15);
        String userId2 = "id";
        User user2 = new User(userId2, date2);
        Reservation reservation2 = new Reservation(user2, false);
        LocalDateTime dateTime = LocalDate.of(2500, 5, 5).atStartOfDay();
        reservation2.setCheckIn(dateTime);
        this.em.persist(reservation);
        this.em.persist(reservation2);
        var reservations = this.repository.listReservation(date2);
        assertThat(reservations.iterator().next().getUserId().equals(user2.getUserId()));
        var lastCheckInByUser = repository.findLastCheckInByUser("id");
        assertThat(Optional.of(lastCheckInByUser).map(Reservation::getCheckIn).orElse(LocalDateTime.now()))
                .isEqualTo(dateTime);
    }
}
