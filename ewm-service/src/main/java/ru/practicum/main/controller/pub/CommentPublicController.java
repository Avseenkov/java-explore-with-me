package ru.practicum.main.controller.pub;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.CommentShortDto;
import ru.practicum.main.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("comments")
@AllArgsConstructor
public class CommentPublicController {

    private CommentService commentService;

    @GetMapping("/{eventId}")
    public List<CommentShortDto> getComments(@PathVariable Long eventId) {
        return commentService.getComments(eventId);
    }
}
