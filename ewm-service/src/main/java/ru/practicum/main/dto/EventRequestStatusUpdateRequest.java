package ru.practicum.main.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private String status;
}
