package ru.practicum.main.controller.pub;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequestMapping("compilations")
@AllArgsConstructor
public class CompilationPublicController {

    private CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(
            @RequestParam(defaultValue = "false") boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return compilationService.getCompilations(pinned, from, size);
    }
}
