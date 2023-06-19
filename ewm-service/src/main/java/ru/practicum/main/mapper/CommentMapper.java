package ru.practicum.main.mapper;

import ru.practicum.main.dto.CommentShortDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.model.Comment;
import ru.practicum.main.utils.Settings;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static CommentShortDto toCommentShortDto(Comment comment) {
        CommentShortDto commentShortDto = CommentShortDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .createdAt(Settings.getFormatter().format(comment.getCreatedAt()))
                .build();

        return commentShortDto;
    }

    public static Comment toComment(NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        return comment;
    }

    public static List<CommentShortDto> mapToCommentShortDto(List<Comment> comments) {
        List<CommentShortDto> commentShortDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentShortDtos.add(toCommentShortDto(comment));
        }
        return commentShortDtos;
    }

}
