package br.com.meli.soccer.match_manager.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record MatchRequestDTO(
        @NotNull
        String homeClubId,

        @NotNull
        String visitingClubId,

        @NotNull
        Integer homeClubGoals,

        @NotNull
        Integer visitingClubGoals,

        @NotNull
        String stadiumId,

        @NotNull
        @PastOrPresent
        LocalDateTime dateTime
){
}
