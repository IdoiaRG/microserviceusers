package com.training.microserviceusers.service;

import com.training.microserviceusers.entity.UsersEntity;
import com.training.microserviceusers.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsersService {
    private UsersRepository usersRepository;
    private ModelMapper modelMapper;
    public UsersService(UsersRepository usersRepository, ModelMapper modelMapper) {
        super();
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    public List<UsersEntity> getAll() {
        log.info("Getting all users");
        return usersRepository.findAll();
    }

    public UsersEntity getUsersById(Long id) {
        UsersEntity users = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Users with id " + id + " not found"));

        return users;
    }

    public List<UsersEntity> getUsersByName(String name) {
        List<UsersEntity> users = usersRepository.findByName(name);
        if (users.isEmpty()) throw new EntityNotFoundException("Users with name " + name + " not found");

        return users;
    }

    public List<UsersEntity> getUsersByPhone(Integer phone){
        List<UsersEntity> users = usersRepository.findByPhone(phone);
        if (users.isEmpty()) throw new EntityNotFoundException("Users with phone " + phone + " not found");

        return users;
    }

    public UsersEntity postUsers(UsersEntity newUsers){
        return usersRepository.save(newUsers);
    }

    public UsersEntity patchUsersById(Long id, UsersEntity newUsers){
        UsersEntity users = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Users with id " + id + " not found"));

        newUsers.setId(id);

        modelMapper.map(newUsers, users);

        return usersRepository.save(users);
    }

    public void deleteUsersById(Long id) {
        UsersEntity users = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Users with id " + id + " not found"));

        usersRepository.deleteById(id);
    }
}
