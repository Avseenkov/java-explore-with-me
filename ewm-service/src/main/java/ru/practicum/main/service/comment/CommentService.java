package ru.practicum.main.service.comment;

import ru.practicum.main.dto.CommentShortDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    CommentShortDto createComment(NewCommentDto newCommentDto, Long userId, Long eventId);

    List<CommentShortDto> getComments(Long eventId);

    void deleteByOwner(Long userId, Long commentId);

    CommentShortDto changeCommentByUser(UpdateCommentDto commentDto, Long userId, Long commentId);

    void deleteByOAdmin(Long commentId);

    CommentShortDto changeCommentByAdmin(UpdateCommentDto commentDto, Long commentId);

}
