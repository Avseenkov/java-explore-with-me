package ru.practicum.main.service.compilation;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequestDto;
import ru.practicum.main.mapper.CompilationMapper;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.QCompilation;
import ru.practicum.main.model.QEvent;
import ru.practicum.main.repository.compilation.CompilationRepository;
import ru.practicum.main.repository.event.EventRepository;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private CompilationRepository compilationRepository;
    private EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {

        Compilation compilation = CompilationMapper.toCompilation(compilationDto);

        if (compilationDto.getEvents() != null) {
            BooleanExpression in = QEvent.event.id.in(compilationDto.getEvents());
            Iterable<Event> events = eventRepository.findAll(in);
            CompilationMapper.setEvens(compilation, Lists.newArrayList(events));
        } else compilation.setEvents(new HashSet<>());

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long id) {
        Compilation compilation = compilationRepository.getCompilationFromDb(id);
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequestDto compilationRequestDto, Long compilationId) {
        Compilation compilation = compilationRepository.getCompilationFromDb(compilationId);

        if (compilationRequestDto.getPinned() != null) {
            compilation.setPinned(compilationRequestDto.getPinned());
        }

        if (compilationRequestDto.getTitle() != null) {
            compilation.setTitle(compilationRequestDto.getTitle());
        }

        if (compilationRequestDto.getEvents() != null) {
            BooleanExpression in = QEvent.event.id.in(compilationRequestDto.getEvents());

            Iterable<Event> events = eventRepository.findAll(in);
            CompilationMapper.setEvens(compilation, Lists.newArrayList(events));
        }

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto getCompilation(Long id) {
        return CompilationMapper.toCompilationDto(compilationRepository.getCompilationFromDb(id));
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);

        BooleanBuilder builder = new BooleanBuilder();
        if (pinned) {
            builder.and(QCompilation.compilation.pinned.eq(true));
            return CompilationMapper.mapToCompilationDto(compilationRepository.findAll(builder.getValue(), pageRequest));
        }

        return CompilationMapper.mapToCompilationDto(compilationRepository.findAll(pageRequest));
    }
}
