package br.com.meli.soccer.match_manager.club.dto.request;

import br.com.meli.soccer.match_manager.common.constants.SchemaConstants;
import br.com.meli.soccer.match_manager.common.validation.PositiveNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClubResult(
        @Schema(description = SchemaConstants.CLUB.ID_DESC, example =  SchemaConstants.CLUB.ID_EXAMPLE)
        @NotEmpty
        String id,

        @NotNull
        @PositiveNumber
        @Schema(description = SchemaConstants.CLUB.GOALS_DESC, example = SchemaConstants.CLUB.GOALS_EXAMPLE)
        Integer goals
) { }
