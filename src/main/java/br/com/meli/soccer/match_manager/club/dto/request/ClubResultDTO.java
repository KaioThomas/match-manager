package br.com.meli.soccer.match_manager.club.dto.request;

import br.com.meli.soccer.match_manager.common.validation.PositiveNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClubResultDTO(
        @NotEmpty
        String id,

        @NotNull
        @PositiveNumber
        Integer goals
) { }
