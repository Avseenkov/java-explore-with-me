package ru.practicum.main.service.compilation;

import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequestDto;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long id);

    CompilationDto updateCompilation(UpdateCompilationRequestDto compilationRequestDto, Long compilationId);

    CompilationDto getCompilation(Long id);

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);
}
