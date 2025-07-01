package br.com.meli.soccer.match_manager.controller;

import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    public ResponseEntity<Club> createClub(@Valid @RequestBody ClubCreateRequestDTO clubCreateRequestDTO) {
        Club response = this.clubService.createClub(clubCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Club> changeClub(@Valid @RequestBody ClubUpdateRequestDTO clubUpdateRequestDTO) {
        Club response = this.clubService.updateClub(clubUpdateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteClub(@PathVariable Long id) {
        this.clubService.delete(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("{id}")
    public ResponseEntity<Club> getClub(@PathVariable Long id) {
        Club response = this.clubService.getClub(id);
        return ResponseEntity.ok(response);

    }
}
