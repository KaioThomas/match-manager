package br.com.meli.soccer.match_manager.match.dto.request;

import br.com.meli.soccer.match_manager.club.dto.request.ClubResult;
import br.com.meli.soccer.match_manager.common.constants.SchemaConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class MatchUpdateRequest extends MatchRequest {
        @NotNull
        @Schema(description = SchemaConstants.MATCH.ID_DESC, example = SchemaConstants.MATCH.ID_EXAMPLE)
        String id;

        public MatchUpdateRequest(String id, ClubResult homeClubResult, ClubResult visitingClubResult, String stadiumId, LocalDateTime dateTime) {
                super(homeClubResult, visitingClubResult, stadiumId, dateTime);
                this.id = id;
        }
}