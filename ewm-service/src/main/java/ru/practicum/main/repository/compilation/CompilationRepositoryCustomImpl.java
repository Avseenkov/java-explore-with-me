package ru.practicum.main.repository.compilation;

import org.springframework.context.annotation.Lazy;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Compilation;

public class CompilationRepositoryCustomImpl implements CompilationRepositoryCustom {

    private CompilationRepository compilationRepository;

    public CompilationRepositoryCustomImpl(@Lazy CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public Compilation getCompilationFromDb(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with %s not found", id)));
    }
}
