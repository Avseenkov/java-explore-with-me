package ru.practicum.main.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @Column
    @Size(min = 20, max = 7000)
    @NotBlank
    private String description;

    @Column
    private LocalDateTime eventDate;

    private Location location;

    @Column
    private boolean paid;

    @Column
    private int participantLimit;

    @Column
    private boolean requestModeration;

    @Column
    @Size(min = 3, max = 120)
    private String title;

    @Column
    @CreationTimestamp
    private LocalDateTime publishedOn;

    @Column
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Formula(
            "(SELECT COALESCE(COUNT(DISTINCT R.id),0) FROM REQUESTS R WHERE R.event_id = ID AND R.status = 'CONFIRMED')"
    )
    private long confirmedRequests;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    private void onPrePersist() {
        createdOn = LocalDateTime.now();
        publishedOn = LocalDateTime.now();
    }
}
