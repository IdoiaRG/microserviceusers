package com.training.microserviceusers.controller;

import com.training.microserviceusers.entity.UsersEntity;
import com.training.microserviceusers.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UsersController {
    private UsersService usersService;
    public UsersController(UsersService usersService) {
        super();
        this.usersService = usersService;
    }

    @GetMapping
    public List<UsersEntity> getAll(){
        return usersService.getAll();
    }

    @GetMapping("/id/{id}")
    public UsersEntity getUsersById(@PathVariable Long id) throws Exception {
        return usersService.getUsersById(id);
    }

    @GetMapping("/name/{name}")
    public List<UsersEntity> getUsersByName(@PathVariable String name){
        return usersService.getUsersByName(name);
    }

    @GetMapping("/phone/{phone}")
    public List<UsersEntity> getUsersByStatus(@PathVariable Integer phone){
        return usersService.getUsersByPhone(phone);
    }

    @PostMapping
    public UsersEntity postUsers(@RequestBody UsersEntity users){
        return usersService.postUsers(users);
    }

    @PatchMapping("/id/{id}")
    public UsersEntity patchUsersById(@PathVariable Long id, @RequestBody UsersEntity user){
        return usersService.patchUsersById(id, user);
    }

    @DeleteMapping("/id/{id}")
    public void deleteUsersById(@PathVariable Long id){
        usersService.deleteUsersById(id);
    }
}