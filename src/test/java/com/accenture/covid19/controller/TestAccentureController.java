//package com.accenture.covid19.controller;
//
//import com.accenture.covid19.Covid19ApplicationTests;
//import com.accenture.covid19.dto.User;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.time.LocalDate;
//
//import static org.junit.Assert.assertEquals;
//
//public class TestAccentureController extends Covid19ApplicationTests {
//
//    @Override
//    @Before
//    public void setUp() {
//        super.setUp();
//    }
//    @Test
//    public void register() throws Exception {
//        String uri = "/register";
//        User user = new User();
//        user.setUserId("id");
//        user.setDate(LocalDate.of(2020, 6, 15));
//
//        String inputJson = super.mapToJson(user);
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
//                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
//
//        int status = mvcResult.getResponse().getStatus();
//        assertEquals(200, status);
//        String content = mvcResult.getResponse().getContentAsString();
//        assertEquals(content, "Product is created successfully");
//    }
//}
