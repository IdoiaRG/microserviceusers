package com.training.microserviceusers.repository;

import com.training.microserviceusers.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity,Long> {
    List<UsersEntity> findByName(String name);
    List<UsersEntity> findByPhone(Integer phone);
    List<UsersEntity> findByEmail(String email);
}
