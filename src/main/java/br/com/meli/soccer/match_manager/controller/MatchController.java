package br.com.meli.soccer.match_manager.controller;

import br.com.meli.soccer.match_manager.model.dto.request.MatchRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.filter.MatchFilterRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchResponseDTO> create(@RequestBody @Valid MatchRequestDTO matchRequestDTO) {
        MatchResponseDTO matchResponseDTO = this.matchService.create(matchRequestDTO);
        return ResponseEntity.ok(matchResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable final String id) {
        this.matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> getById(@PathVariable final String id) {
        MatchResponseDTO matchResponseDTO = this.matchService.getById(id);
        return ResponseEntity.ok(matchResponseDTO);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<MatchResponseDTO>> getAll(@ModelAttribute MatchFilterRequestDTO matchFilterRequestDTO) {
        List<MatchResponseDTO> matchResponseDTOS = this.matchService.getAll(matchFilterRequestDTO);
        return ResponseEntity.ok(matchResponseDTOS);
    }
}
