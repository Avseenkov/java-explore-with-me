package ru.practicum.main.repository.comment;

import ru.practicum.main.model.Comment;

public interface CommentRepositoryCustom {
    Comment getCommentFromDb(Long id);
}
