package com.training.microserviceusers.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Table(name = "Users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String sex;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "birth")
    private LocalDate birthDate;
    @NonNull
    @Min(value = 600000000, message = "Phone number should have 9 numbers and start with 6 o 7")
    @Max(value = 799999999, message = "Phone number should have 9 numbers and start with 6 o 7")
    private Integer phone;
    @NonNull
    @Email
    private String email;
    @NonNull
    @Column(name = "work_position")
    private String workPosition;
    @NonNull
    @PositiveOrZero
    @Max(99999)
    private Integer cp;
}
