package com.training.microserviceusers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.training.microserviceusers.entity.UsersEntity;
import com.training.microserviceusers.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/data-test.sql", executionPhase = BEFORE_TEST_METHOD)
public class UsersIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UsersService usersService;

    UsersEntity user1 = new UsersEntity(1L, "Ivan", "Castuera", "Male",
            LocalDate.of(1999, 06, 15), 777777777, "incz@gft.com",
            "Officer", 22520);

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void getAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    void getUsersByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{id:1,name:Idoia,surname:Redorta,sex:Female,birthDate:1994-12-21,phone:666666666," +
                                "email:iarz@gft.com,workPosition:Graduate,cp:25001}"));
    }

    @Test
    void getUsersByIdNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}",6L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersByNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/name/{name}","Idoia"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{id:1,name:Idoia,surname:Redorta,sex:Female,birthDate:1994-12-21,phone:666666666," +
                        "email:iarz@gft.com,workPosition:Graduate,cp:25001}," +
                        "{id:3,name:Idoia,surname:Martínez,sex:Female,birthDate:1988-06-07,phone:677889900," +
                        "email:idoia.martinez@example.com,workPosition:Developer,cp:28004}]"));
    }

    @Test
    void getUsersByNameNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/name/{name}","Kora"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersByPhoneTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/phone/{phone}",677889900))
                .andExpect(status().isOk())
                .andExpect(content().json("[{id:3,name:Idoia,surname:Martínez,sex:Female,birthDate:1988-06-07,phone:677889900," +
                        "email:idoia.martinez@example.com,workPosition:Developer,cp:28004}," +
                        "{id:4,name:Sofía,surname:López,sex:Female,birthDate:1997-11-03,phone:677889900," +
                        "email:sofia.lopez@example.com,workPosition:Designer,cp:28005}]"));
    }

    @Test
    void getUsersByPhoneNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/phone/{phone}",677889901))
                .andExpect(status().isNotFound());
    }

    @Test
    void postUsersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(objectMapper.writeValueAsString(user1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user1)));
    }

    @Test
    void patchUsersByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/id/{id}", 1L)
                        .content(objectMapper.writeValueAsString(user1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user1)));
    }

    @Test
    void patchUsersByIdNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/id/{id}", 7L)
                        .content(objectMapper.writeValueAsString(user1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUsersByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUsersByIdNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/{id}", 7L))
                .andExpect(status().isNotFound());
    }
}