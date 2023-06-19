package ru.practicum.main.controller.priv;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CommentShortDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.dto.UpdateCommentDto;
import ru.practicum.main.service.comment.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
@AllArgsConstructor
@Validated
public class CommentPrivateController {

    private CommentService commentService;


    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByOwner(
            @PathVariable Long userId,
            @PathVariable Long commentId
    ) {
        commentService.deleteByOwner(userId, commentId);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentShortDto changeCommentByUser(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentDto commentDto
    ) {
        return commentService.changeCommentByUser(
                commentDto,
                userId,
                commentId
        );
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentShortDto createComment(
            @PathVariable Long userId,
            @RequestParam Long eventId,
            @RequestBody @Valid NewCommentDto commentDto
    ) {
        return commentService.createComment(commentDto, userId, eventId);
    }
}
