package ru.practicum.main.service.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CommentShortDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.dto.UpdateCommentDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.CommentMapper;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.comment.CommentRepository;
import ru.practicum.main.repository.event.EventRepository;
import ru.practicum.main.repository.user.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private UserRepository userRepository;

    private EventRepository eventRepository;

    @Override
    public CommentShortDto createComment(NewCommentDto newCommentDto, Long userId, Long eventId) {

        User user = userRepository.getUserFromDb(userId);

        Event event = eventRepository.getEventFromDb(eventId);

        Comment comment = CommentMapper.toComment(newCommentDto);

        comment.setAuthor(user);
        comment.setEvent(event);

        return CommentMapper.toCommentShortDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentShortDto> getComments(Long eventId) {
        return CommentMapper.mapToCommentShortDto(commentRepository.findByEvent_Id(eventId));
    }

    @Override
    public void deleteByOwner(Long userId, Long commentId) {
        User user = userRepository.getUserFromDb(userId);
        Comment comment = commentRepository.getCommentFromDb(commentId);

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new NotFoundException("Comment not found");
        }

        commentRepository.delete(comment);
    }

    @Override
    public CommentShortDto changeCommentByUser(UpdateCommentDto commentDto, Long userId, Long commentId) {
        User user = userRepository.getUserFromDb(userId);
        Comment comment = commentRepository.getCommentFromDb(commentId);

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new NotFoundException("Comment not found");
        }

        comment.setText(commentDto.getText());

        return CommentMapper.toCommentShortDto(commentRepository.save(comment));
    }

    @Override
    public void deleteByOAdmin(Long commentId) {
        Comment comment = commentRepository.getCommentFromDb(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public CommentShortDto changeCommentByAdmin(UpdateCommentDto commentDto, Long commentId) {

        Comment comment = commentRepository.getCommentFromDb(commentId);

        comment.setText(commentDto.getText());

        return CommentMapper.toCommentShortDto(commentRepository.save(comment));
    }
}
