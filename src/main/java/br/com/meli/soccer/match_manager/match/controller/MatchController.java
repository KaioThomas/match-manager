package br.com.meli.soccer.match_manager.match.controller;

import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import br.com.meli.soccer.match_manager.match.dto.MatchTotalRetrospect;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchFilterRequestDTO;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchHistoryResponse;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.match.service.MatchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponseDTO create(
            @RequestBody
            @Valid
            final MatchCreateRequest matchCreateRequest
    ) {
        return this.matchService.create(matchCreateRequest);
    }

    @PutMapping
    public MatchResponseDTO create(
            @RequestBody
            @Valid
            final MatchUpdateRequest matchUpdateRequest
    ) {
        return this.matchService.update(matchUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(
            @PathVariable
            final String id
    ) {
        this.matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public MatchResponseDTO getById(
            @PathVariable
            final String id
    ) {
        return this.matchService.getById(id);
    }

    @GetMapping("/findAll")
    public List<MatchResponseDTO> getAll(
            @ModelAttribute
            final MatchFilterRequestDTO matchFilterRequestDTO,

            final Pageable pageable
    ) {
        return this.matchService.getAll(matchFilterRequestDTO, pageable);
    }

    @GetMapping("/retrospect")
    public MatchHistoryResponse getMatchRetrospectByOpponent(
            @RequestParam
            final String clubId,

            @RequestParam(required = false)
            final String opponentId,

            @RequestParam(required = false)
            final ClubTypeEnum clubRequiredActing
    ) {
        return this.matchService.getMatchHistoryByOpponent(clubId, clubRequiredActing, opponentId);
    }

    @GetMapping("/retrospect/all")
    public MatchTotalRetrospect getMatchRetrospect(
            @NotNull
            @RequestParam
            final String clubId,

            @RequestParam(required = false)
            final ClubTypeEnum clubRequiredActing
    ) {
        return this.matchService.getMatchRetrospect(clubId, clubRequiredActing);
    }
}
