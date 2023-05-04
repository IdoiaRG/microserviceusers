package com.training.microserviceusers;

import com.training.microserviceusers.entity.UsersEntity;
import com.training.microserviceusers.repository.UsersRepository;
import com.training.microserviceusers.service.UsersService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @InjectMocks
    private UsersService usersService;
    @Mock
    private UsersRepository usersRepository;

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

    String idNotFoundMessage = "Users with id 1 not found";

    @Test
    void getAllTest() {
        given(usersRepository.findAll()).willReturn(usersList);

        assertThat(usersService.getAll()).isEqualTo(usersList);
    }

    @Test
    void getUsersByIdTest() throws Exception {
        given(usersRepository.findById(1L)).willReturn(Optional.of(user1));

        assertThat(usersService.getUsersById(1L)).isEqualTo(user1);
    }

    @Test
    void getUsersByIdNotFoundTest() {
        given(usersRepository.findById(1L)).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> usersService.getUsersById(1L));

        assertEquals(idNotFoundMessage, exception.getMessage());
    }

    @Test
    void getUsersByNameTest() {
        given(usersRepository.findByName("Idoia")).willReturn(usersListEqual);

        assertThat(usersService.getUsersByName("Idoia")).isEqualTo(usersListEqual);
    }

    @Test
    void getUsersByNameNotFoundTest() {
        given(usersRepository.findByName(any())).willReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> usersService.getUsersByName("Kora"));

        assertEquals("Users with name Kora not found", exception.getMessage());
    }

    @Test
    void getUsersByPhoneTest(){
        given(usersRepository.findByPhone(666666666)).willReturn(usersListEqual);

        assertThat(usersService.getUsersByPhone(666666666)).isEqualTo(usersListEqual);
    }

    @Test
    void getUsersByPhoneNotFoundTest() {
        given(usersRepository.findByPhone(any())).willReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> usersService.getUsersByPhone(888888888));

        assertEquals("Users with phone 888888888 not found", exception.getMessage());
    }

    @Test
    void postUsers(){
        given(usersRepository.save(any())).willReturn(user1);

        UsersEntity result = usersService.postUsers(user1);

        verify(usersRepository).save(any());
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void patchUsersByIdTest(){
        given(usersRepository.save(any())).willReturn(user1);
        given(usersRepository.findById(any())).willReturn(Optional.of(user1));

        UsersEntity result = usersService.patchUsersById(1L, user1);

        verify(usersRepository).save(any());
        verify(usersRepository).findById(1L);
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void patchUsersByIdNotFoundTest(){
        given(usersRepository.findById(any())).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> usersService.patchUsersById(1L, user1));

        assertEquals(idNotFoundMessage, exception.getMessage());
    }

    @Test
    void deleteUsersByIdTest(){
        doNothing().when(usersRepository).deleteById(1L);
        given(usersRepository.findById(1L)).willReturn(Optional.of(user1));

        usersService.deleteUsersById(1L);

        verify(usersRepository).deleteById(1L);
    }

    @Test
    void deleteUsersByIdNotFoundTest(){
        given(usersRepository.findById(1L)).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> usersService.deleteUsersById(1L));

        assertEquals(idNotFoundMessage, exception.getMessage());
    }
}