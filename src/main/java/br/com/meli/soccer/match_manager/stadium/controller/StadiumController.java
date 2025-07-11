package br.com.meli.soccer.match_manager.stadium.controller;

import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumCreateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumUpdateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.stadium.service.StadiumService;
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
    public StadiumResponseDTO create(
            @Valid
            @RequestBody
            final StadiumCreateRequest stadiumCreateRequest
    ) {
        return this.stadiumService.create(stadiumCreateRequest);
    }

    @PutMapping
    public StadiumResponseDTO update(
            @Valid
            @RequestBody
            final StadiumUpdateRequest stadiumUpdateRequest
    ) {
        return this.stadiumService.update(stadiumUpdateRequest);
    }

    @GetMapping("/{id}")
    public StadiumResponseDTO getById(
            @PathVariable final String id
    ) {
        return this.stadiumService.getById(id);
    }

    @GetMapping("/findAll")
    public List<StadiumResponseDTO> getAll(
            @RequestParam(required = false)
            final String name,

            final Pageable pageable
    ) {
        return this.stadiumService.getAll(name, pageable);
    }

}
