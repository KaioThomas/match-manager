package br.com.meli.soccer.match_manager.controller;

import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.club.ClubResponseDTO;
import br.com.meli.soccer.match_manager.service.ClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ResponseEntity<ClubResponseDTO> create(@Valid @RequestBody ClubCreateRequestDTO clubCreateRequestDTO) {
        ClubResponseDTO response = this.clubService.create(clubCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<ClubResponseDTO> update(@Valid @RequestBody ClubUpdateRequestDTO clubUpdateRequestDTO) {
        ClubResponseDTO response = this.clubService.update(clubUpdateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable UUID id) {
        this.clubService.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubResponseDTO> getById(@PathVariable UUID id) {
        ClubResponseDTO response = this.clubService.getById(id);
        return ResponseEntity.ok(response);
    }

    }
}
