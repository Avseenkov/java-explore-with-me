package ru.practicum.main.repository.compilation;

import ru.practicum.main.model.Compilation;

public interface CompilationRepositoryCustom {

    Compilation getCompilationFromDb(Long id);

}
