package br.com.meli.soccer.match_manager.club.dto.request;

import br.com.meli.soccer.match_manager.common.enums.AcronymStatesEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import br.com.meli.soccer.match_manager.common.constants.SchemaConstants.CLUB;

import java.time.LocalDate;


@ToString
@Getter
@Setter
public class ClubUpdateRequest extends ClubRequest {

    @NotEmpty
    @Schema(description = CLUB.ID_DESC, example = CLUB.ID_EXAMPLE)
    private String id;

    public ClubUpdateRequest(String id, String name, AcronymStatesEnum stateAcronym, LocalDate creationDate, Boolean active) {
        super(name, stateAcronym, creationDate, active);
        this.id = id;
    }
}
