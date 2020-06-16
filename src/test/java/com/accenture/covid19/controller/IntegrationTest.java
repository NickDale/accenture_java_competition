package com.accenture.covid19.controller;

import com.accenture.covid19.dto.User;
import com.accenture.covid19.model.Reservation;
import com.accenture.covid19.repository.ReservationRepository;
import com.accenture.covid19.service.RegisterService;
import com.accenture.covid19.service.RegisterServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class IntegrationTest {

    @Autowired
    private RegisterService service;
    @MockBean
    private ReservationRepository repo;

    @Before
    public void setUp() {
        Reservation r = new Reservation(new User("userId,", LocalDate.of(2020, 6, 15)), false);
        r.setCheckIn(LocalDateTime.now());
        Mockito.when(repo.findLastCheckInByUser(r.getUserId()))
                .thenReturn(r);
    }

    @Test
    public void register() throws Exception {
        String userId = "userId";
        Reservation found = service.findLastCheckInByUser(userId);

        assertThat(ofNullable(found).map(Reservation::getUserId).orElse("TEST"))
                .isEqualTo(userId);
    }

    @TestConfiguration
    static class RegisterServiceImplTestContextConfiguration {

        @Bean
        public RegisterService employeeService() {
            return new RegisterServiceImpl();
        }
    }
}