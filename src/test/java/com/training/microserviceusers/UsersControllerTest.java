package com.training.microserviceusers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.training.microserviceusers.controller.UsersController;
import com.training.microserviceusers.entity.UsersEntity;
import com.training.microserviceusers.service.UsersService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
public class UsersControllerTest {
    @MockBean
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    @Autowired
    private MockMvc mockMvc;

    UsersEntity user1 = new UsersEntity(1L, "Idoia", "Redorta", "Female",
            LocalDate.of(1994, 12, 21), 666666666, "iarz@gft.com",
            "Graduate", 25001);
    UsersEntity user2 = new UsersEntity(2L, "Ivan", "Castuera", "Male",
            LocalDate.of(1999, 06, 15), 777777777, "incz@gft.com",
            "Officer", 22520);
    UsersEntity user3 = new UsersEntity(3L, "Idoia", "Redorta", "Female",
            LocalDate.of(1994, 12, 21), 666666666, "iarz@gft.com",
            "Graduate", 25001);
    List<UsersEntity> usersList = Arrays.asList(user1, user2, user3);
    List<UsersEntity> usersListEqual = Arrays.asList(user1, user3);

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void getAllTest() throws Exception {
        given(usersService.getAll()).willReturn(usersList);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(content().json(objectMapper.writeValueAsString(usersList)))
                .andReturn();
    }

    @Test
    void getUsersByIdTest() throws Exception {
        given(usersService.getUsersById(1L)).willReturn(user1);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/1"))
                .andExpect(content().json(objectMapper.writeValueAsString(user1)))
                .andReturn();
    }

    @Test
    void getUsersByNameTest() throws Exception {
        given(usersService.getUsersByName("Idoia")).willReturn(usersListEqual);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/name/Idoia"))
                .andExpect(content().json(objectMapper.writeValueAsString(usersListEqual)))
                .andReturn();
    }

    @Test
    void getUsersByPhoneTest() throws Exception {
        given(usersService.getUsersByPhone(666666666)).willReturn(usersListEqual);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/phone/666666666"))
                .andExpect(content().json(objectMapper.writeValueAsString(usersListEqual)))
                .andReturn();
    }

    @Test
    void postUsersTest() throws Exception {
        given(usersService.postUsers(user1)).willReturn(user1);

        mockMvc.perform(MockMvcRequestBuilders.post("/users"))
                .andExpect(content().json(objectMapper.writeValueAsString(user1)))
                .andReturn();
    }

    @Test
    void patchUsersByIdTest() throws Exception{
        given(usersService.patchUsersById(1L, user3)).willReturn(user1);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/id/1", user3))
                .andExpect(content().json(objectMapper.writeValueAsString(user1)))
                .andReturn();
    }

    @Test
    void deleteUsersByIdTest() throws Exception {
        doNothing().when(usersService).deleteUsersById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/1"))
                .andExpect(status().isOk());

        verify(usersService).deleteUsersById(1L);
    }
}