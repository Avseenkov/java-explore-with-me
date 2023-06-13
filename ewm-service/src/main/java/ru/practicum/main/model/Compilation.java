package ru.practicum.main.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private boolean pinned;

    @Column
    private String title;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "COMPILATION_EVENT",
            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID")
    )
    private Set<Event> events;
}
