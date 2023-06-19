package ru.practicum.main.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CommentShortDto;
import ru.practicum.main.dto.UpdateCommentDto;
import ru.practicum.main.service.comment.CommentService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("admin")
@Validated
public class CommentAdminController {

    private CommentService commentService;


    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(
            @PathVariable Long commentId
    ) {
        commentService.deleteByOAdmin(commentId);
    }

    @PatchMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentShortDto changeCommentByAdmin(
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentDto commentDto
    ) {
        return commentService.changeCommentByAdmin(
                commentDto,
                commentId
        );
    }
}
