package br.com.meli.soccer.match_manager.match.dto.request;

import br.com.meli.soccer.match_manager.club.dto.request.ClubResultDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record MatchCreateRequest(
        @Valid
        ClubResultDTO homeClubResult,

        @Valid
        ClubResultDTO visitingClubResult,

        @NotEmpty
        String stadiumId,

        @NotNull
        @PastOrPresent
        LocalDateTime dateTime
) implements MatchRequestDTO { }
