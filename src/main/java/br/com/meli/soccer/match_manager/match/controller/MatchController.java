package br.com.meli.soccer.match_manager.match.controller;

import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import br.com.meli.soccer.match_manager.match.dto.response.ClubTotalRetrospectResponse;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchFilterRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponse;
import br.com.meli.soccer.match_manager.match.dto.response.RankingResponse;
import br.com.meli.soccer.match_manager.match.dto.response.RetrospectByOpponentResponse;
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
            final String id
    ) {
        this.matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public MatchResponse getById(
            @PathVariable
            final String id
    ) {
        return this.matchService.getById(id);
    }

    @GetMapping("/findAll")
    public List<MatchResponse> getAll(
            @ModelAttribute
            final MatchFilterRequest matchFilterRequest,

            final Pageable pageable
    ) {
        return this.matchService.getAll(matchFilterRequest, pageable);
    }

    @GetMapping("/retrospect/total")
    public List<RetrospectByOpponentResponse> getTotalRetrospect(
            @RequestParam
            final String clubId,

            @RequestParam(required = false)
            final String opponentId,

            @RequestParam(required = false)
            final ClubTypeEnum clubRequiredActing
    ) {
        return this.matchService.getTotalRetrospect(clubId, clubRequiredActing, opponentId);
    }

    @GetMapping("/retrospect")
    public ClubTotalRetrospectResponse getRetrospectByOpponent(
            @NotNull
            @RequestParam
            final String clubId,

            @RequestParam(required = false)
            final ClubTypeEnum clubRequiredActing
    ) {
        return this.matchService.getMatchRetrospect(clubId, clubRequiredActing);
    }

    @GetMapping("/ranking")
    public List<RankingResponse> getRanking() {
        return this.matchService.getRanking();
    }
}
