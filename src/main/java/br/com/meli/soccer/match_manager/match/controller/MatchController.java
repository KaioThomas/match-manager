package br.com.meli.soccer.match_manager.match.controller;

import br.com.meli.soccer.match_manager.common.constants.SchemaConstants.MATCH;
import br.com.meli.soccer.match_manager.common.constants.SchemaConstants.CLUB;
import br.com.meli.soccer.match_manager.match.dto.GeneralRetrospect;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchActingFilter;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchThrashingFilter;
import br.com.meli.soccer.match_manager.match.dto.response.*;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.service.MatchService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
    public MatchResponse create(
            @RequestBody
            @Valid
            final MatchCreateRequest matchCreateRequest
    ) {
        return this.matchService.create(matchCreateRequest);
    }

    @PutMapping
    public MatchResponse update(
            @RequestBody
            @Valid
            final MatchUpdateRequest matchUpdateRequest
    ) {
        return this.matchService.update(matchUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(
            @PathVariable
            @Parameter(description = MATCH.ID_DESC, example = MATCH.ID_EXAMPLE)
            final String id
    ) {
        this.matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public MatchResponse getById(
            @PathVariable
            @Parameter(description = MATCH.ID_DESC, example = MATCH.ID_EXAMPLE)
            final String id
    ) {
        return this.matchService.getById(id);
    }

    @GetMapping("/findAll")
    public List<MatchResponse> getAll(
            @Parameter(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            @RequestParam(required = false)
            final String clubId,

            @ParameterObject
            @Valid
            final MatchThrashingFilter matchThrashingFilter,

            @ParameterObject
            final Pageable pageable
    ) {
        return this.matchService.getAll(clubId, matchThrashingFilter, pageable);
    }

    @GetMapping("/retrospect/general")
    public GeneralRetrospect getGeneralRetrospect(
            @NotNull
            @RequestParam
            @Schema(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            final String clubId,

            @ParameterObject
            final MatchActingFilter matchActingFilter
    ) {
        return this.matchService.getGeneralRetrospect(clubId, matchActingFilter);
    }

    @GetMapping("/retrospect/opponents")
    public List<RetrospectByOpponent> getRetrospectByOpponents(
            @NotEmpty
            @Schema(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            @RequestParam(required = false)
            final String clubId,

            @ParameterObject
            final MatchActingFilter matchActingFilter
    ) {
        return this.matchService.getRetrospectByOpponents(clubId, matchActingFilter);
    }

    @GetMapping("/retrospect/direct-confrontations")
    public DirectConfrontationsResponse getDirectConfrontations(
            @NotEmpty
            @Schema(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            final String clubA_id,

            @NotEmpty
            @Schema(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
            final String clubB_id
    ) {
        return this.matchService.getDirectConfrontations(clubA_id, clubB_id);
    }

    @GetMapping("/ranking")
    public List<RankingResponse> getRanking() {
        return this.matchService.getRanking();
    }
}
