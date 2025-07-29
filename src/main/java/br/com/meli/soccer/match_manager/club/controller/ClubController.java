package br.com.meli.soccer.match_manager.club.controller;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.request.ClubUpdateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.club.service.ClubService;
import br.com.meli.soccer.match_manager.common.constants.SchemaConstants.CLUB;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ResponseEntity<ClubResponse> create(
            @Valid
            @RequestBody
            final ClubCreateRequest clubCreateRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.clubService.create(clubCreateRequest));
    }

    @PutMapping
    public ResponseEntity<ClubResponse> update(
            @Valid
            @RequestBody
            final ClubUpdateRequest clubUpdateRequest
    ) {
        return ResponseEntity.ok(this.clubService.update(clubUpdateRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable
            @Parameter(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            final String id
    ) {
        this.clubService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubResponse> getById(
            @PathVariable
            @Parameter(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            final String id
    ) {
        return ResponseEntity.ok(this.clubService.getById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<ClubResponse>> getAll(
            @RequestParam(required = false)
            @Parameter(description = CLUB.NAME_DESC, example = CLUB.NAME_EXAMPLE)
            final String name,

            @RequestParam(required = false)
            @Parameter(description = CLUB.ACTIVE_DESC, example = CLUB.ACTIVE_EXAMPLE)
            final Boolean active,

            @RequestParam(required = false)
            @Parameter(description = CLUB.ACRONYM_STATE_DESC, example = CLUB.ACRONYM_STATE_EXAMPLE)
            final String acronymState,

            @ParameterObject
            final Pageable pageable
    ) {
        return ResponseEntity.ok(this.clubService.getAll(name, active, acronymState, pageable));
    }
}
