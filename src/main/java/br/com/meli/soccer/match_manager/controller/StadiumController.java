package br.com.meli.soccer.match_manager.controller;

import br.com.meli.soccer.match_manager.model.dto.request.StadiumRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.service.StadiumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stadium")
@RequiredArgsConstructor
public class StadiumController {

    private final StadiumService stadiumService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StadiumResponseDTO create(@RequestBody @Valid final StadiumRequestDTO stadiumRequestDTO) {
        return this.stadiumService.create(stadiumRequestDTO);
    }

    @PutMapping("/{id}")
    public StadiumResponseDTO update(
            @RequestBody @Valid final StadiumRequestDTO stadiumRequestDTO,
            @PathVariable final String id
    ) {
        return this.stadiumService.update(stadiumRequestDTO, id);
    }

    @GetMapping("/{id}")
    public StadiumResponseDTO getById(@PathVariable final String id) {
        return this.stadiumService.getById(id);
    }

    @GetMapping("/findAll")
    public List<StadiumResponseDTO> getAll(
            @RequestParam(required = false) final String name,
            final Pageable pageable
    ) {
        return this.stadiumService.getAll(name, pageable);
    }

}
