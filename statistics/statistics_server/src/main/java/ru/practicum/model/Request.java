package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "requestId")
    private Long id;

    @NotBlank
    private String app;

    @NotBlank
    private String ip;

    @NotNull
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @NotBlank
    private String uri;

}
