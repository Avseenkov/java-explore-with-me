package ru.practicum.main.repository.comment;

import org.springframework.context.annotation.Lazy;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Comment;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private CommentRepository commentRepository;

    public CommentRepositoryCustomImpl(@Lazy CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment getCommentFromDb(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with %s not found", id)));

    }
}
