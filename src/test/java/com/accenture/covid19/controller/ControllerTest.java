package com.accenture.covid19.controller;

import com.accenture.covid19.dto.User;
import com.accenture.covid19.model.Reservation;
import com.accenture.covid19.service.RegisterService;
import com.accenture.covid19.service.RegisterServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@WebMvcTest(AccentureController.class)
@ContextConfiguration(classes = {RegisterServiceImpl.class, Reservation.class})
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegisterService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void register() throws Exception {
        String uri = "/register";
        User user = new User();
        user.setUserId("id");
        user.setDate(LocalDate.of(2020, 6, 15));


        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(user))).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Product is created successfully");
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }
}
