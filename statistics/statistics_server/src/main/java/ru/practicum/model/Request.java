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
    public Long id;

    @NotBlank
    public String app;

    @NotBlank
    public String ip;

    @NotNull
    @Column(name = "createdAt")
    public LocalDateTime createdAt;

    @NotBlank
    public String uri;

}
