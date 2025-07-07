package br.com.meli.soccer.match_manager.controller;

import br.com.meli.soccer.match_manager.model.dto.request.ClubRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.service.ClubService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ClubResponseDTO> create(@Valid @RequestBody ClubRequestDTO clubRequestDTO) {
        ClubResponseDTO response = this.clubService.create(clubRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubResponseDTO> update(
            @Valid @RequestBody ClubRequestDTO clubRequestDTO,
            @PathVariable @NotNull String id
    ) {
        ClubResponseDTO response = this.clubService.update(clubRequestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id) {
        this.clubService.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubResponseDTO> getById(@PathVariable String id) {
        ClubResponseDTO response = this.clubService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClubResponseDTO>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String acronymState,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String orderBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        List<ClubResponseDTO> response = this.clubService.getAll(name, acronymState, active, page, size, orderBy, direction);
        return ResponseEntity.ok(response);
    }
}
