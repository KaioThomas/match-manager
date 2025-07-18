package br.com.meli.soccer.match_manager.match.controller;

import br.com.meli.soccer.match_manager.match.dto.filter.MatchActingFilter;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchThrashingFilter;
import br.com.meli.soccer.match_manager.match.dto.response.ClubTotalRetrospectResponse;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponse;
import br.com.meli.soccer.match_manager.match.dto.response.RankingResponse;
import br.com.meli.soccer.match_manager.match.dto.response.RetrospectByOpponentResponse;
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
            @Parameter(description = "id da partida a ser deletada", example = "08d9cf76-214b-4e40-8811-3b85b4fdad03")
            final String id
    ) {
        this.matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public MatchResponse getById(
            @PathVariable
            @Parameter(description = "id da partida para busca", example = "08d9cf76-214b-4e40-8811-3b85b4fdad03")
            final String id
    ) {
        return this.matchService.getById(id);
    }

    @GetMapping("/findAll")
    public List<MatchResponse> getAll(
            @Parameter(description = "id do clube", example = "e4446984-735d-4d18-a207-dad2632c2645")
            @RequestParam(required = false)
            final String clubId,

            @ParameterObject
            final Pageable pageable,

            @ParameterObject
            @Valid
            final MatchThrashingFilter matchThrashingFilter
    ) {
        return this.matchService.getAll(clubId, matchThrashingFilter, pageable);
    }

    @GetMapping("/retrospect/total")
    public List<RetrospectByOpponentResponse> getTotalRetrospect(
            @NotEmpty
            @Schema(description = "id do clube", example = "e4446984-735d-4d18-a207-dad2632c2645")
            @RequestParam(required = false)
            final String clubId,

            @Schema(description = "id do clube oponente", example = "e4446984-735d-4d18-a207-dad2632c2645")
            @RequestParam(required = false)
            final String opponentId,

            @ParameterObject
            final MatchActingFilter matchActingFilter
    ) {
        return this.matchService.getTotalRetrospect(clubId, matchActingFilter, opponentId);
    }

    @GetMapping("/retrospect/opponents")
    public ClubTotalRetrospectResponse getRetrospectByOpponent(
            @NotNull
            @RequestParam
            @Schema(description = "id do clube", example = "e4446984-735d-4d18-a207-dad2632c2645")
            final String clubId,

            @ParameterObject
            final MatchActingFilter matchActingFilter
    ) {
        return this.matchService.getMatchRetrospectByOpponent(clubId, matchActingFilter);
    }

    @GetMapping("/ranking")
    public List<RankingResponse> getRanking() {
        return this.matchService.getRanking();
    }
}
