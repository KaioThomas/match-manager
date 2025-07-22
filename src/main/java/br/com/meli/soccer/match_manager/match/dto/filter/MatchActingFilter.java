package br.com.meli.soccer.match_manager.match.dto.filter;

import br.com.meli.soccer.match_manager.common.constants.SchemaConstants.MATCH;
import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

public record MatchActingFilter (
    @Schema(description = MATCH.CLUB_ACTING_DESC, example = MATCH.CLUB_ACTING_EXAMPLE)
    ClubTypeEnum clubRequiredActing
) { }
