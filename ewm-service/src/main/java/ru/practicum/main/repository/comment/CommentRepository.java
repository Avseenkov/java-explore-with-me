package ru.practicum.main.repository.comment;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.main.model.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long>, CommentRepositoryCustom {

    List<Comment> findByEvent_Id(Long id);

}
