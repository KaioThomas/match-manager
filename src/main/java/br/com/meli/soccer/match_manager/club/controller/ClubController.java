package br.com.meli.soccer.match_manager.club.controller;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.request.ClubUpdateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.club.service.ClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClubResponseDTO create(
            @Valid
            @RequestBody
            final ClubCreateRequest clubCreateRequest
    ) {
        return this.clubService.create(clubCreateRequest);
    }

    @PutMapping
    public ClubResponseDTO update(
            @Valid
            @RequestBody
            final ClubUpdateRequest clubUpdateRequest
    ) {
        return this.clubService.update(clubUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteById(
            @PathVariable
            final String id
    ) {
        this.clubService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ClubResponseDTO getById(
            @PathVariable
            final String id
    ) {
        return this.clubService.getById(id);
    }

    @GetMapping
    public List<ClubResponseDTO> getAll(
            @RequestParam(required = false)
            final String name,

            @RequestParam(required = false)
            final Boolean active,

            @RequestParam(required = false)
            final String acronymState,

            final Pageable pageable
    ) {
        return this.clubService.getAll(name, active, acronymState, pageable);
    }
}
