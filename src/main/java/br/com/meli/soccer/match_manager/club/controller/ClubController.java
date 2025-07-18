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
    public ClubResponse create(
            @Valid
            @RequestBody
            final ClubCreateRequest clubCreateRequest
    ) {
        return this.clubService.create(clubCreateRequest);
    }

    @PutMapping
    public ClubResponse update(
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
            @Parameter(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            final String id
    ) {
        this.clubService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ClubResponse getById(
            @PathVariable
            @Parameter(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            final String id
    ) {
        return this.clubService.getById(id);
    }

    @GetMapping("/findAll")
    public List<ClubResponse> getAll(
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
        return this.clubService.getAll(name, active, acronymState, pageable);
    }
}
