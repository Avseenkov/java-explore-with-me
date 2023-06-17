package ru.practicum.main.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    @Email
    @NotNull
    @Size(min = 6, max = 254)
    private String email;

    @Column
    @NotNull
    @Size(min = 2, max = 250)
    private String name;

}
