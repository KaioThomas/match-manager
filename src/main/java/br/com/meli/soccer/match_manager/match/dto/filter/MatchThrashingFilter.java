package br.com.meli.soccer.match_manager.match.dto.filter;

import br.com.meli.soccer.match_manager.common.constants.SchemaConstants.MATCH;
import io.swagger.v3.oas.annotations.media.Schema;
public record MatchThrashingFilter(
        @Schema(description = MATCH.THRASHING_DESC, example = MATCH.THRASHING_EXAMPLE)
        Boolean thrashing
) { }
