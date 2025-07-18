package br.com.meli.soccer.match_manager.club.dto.request;

import br.com.meli.soccer.match_manager.club.validation.ValidClubName;
import br.com.meli.soccer.match_manager.common.enums.AcronymStatesEnum;
import br.com.meli.soccer.match_manager.common.constants.SchemaConstants.CLUB;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClubRequest {

    @ValidClubName
    @Schema(description = CLUB.NAME_DESC, example = CLUB.NAME_EXAMPLE)
    private String name;

    @NotNull
    @Schema(description = CLUB.ACRONYM_STATE_DESC, example = CLUB.ACRONYM_STATE_EXAMPLE)
    private AcronymStatesEnum stateAcronym;

    @NotNull
    @PastOrPresent
    @Schema(description = CLUB.DATE_DESC, example = CLUB.DATE_EXAMPLE)
    private LocalDate creationDate;

    @NotNull
    @Schema(description = CLUB.ACTIVE_DESC, example = CLUB.ACTIVE_EXAMPLE)
    private Boolean active;
}