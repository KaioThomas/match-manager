package br.com.meli.soccer.match_manager.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchRequestDTO(
        @NotNull
        UUID homeClubId,

        @NotNull
        UUID visitingClubId,

        @NotNull
        Integer homeClubGoals,

        @NotNull
        Integer visitingClubGoals,

        @NotNull
        UUID stadiumId,

        @NotNull
        @PastOrPresent
        LocalDateTime dateAndHour
){
}
