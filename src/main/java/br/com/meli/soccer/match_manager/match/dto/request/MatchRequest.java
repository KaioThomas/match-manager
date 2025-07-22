package br.com.meli.soccer.match_manager.match.dto.request;

import br.com.meli.soccer.match_manager.club.dto.request.ClubResult;
import br.com.meli.soccer.match_manager.common.constants.SchemaConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class MatchRequest {
        @Valid
        private ClubResult homeClubResult;

        @Valid
        private ClubResult visitingClubResult;

        @NotEmpty
        @Schema(description = SchemaConstants.STADIUM.ID_DESC, example = SchemaConstants.STADIUM.ID_EXAMPLE)
        private String stadiumId;

        @NotNull
        @PastOrPresent
        private LocalDateTime dateTime;
}
