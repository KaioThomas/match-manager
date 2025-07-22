package br.com.meli.soccer.match_manager.club.dto.request;

import br.com.meli.soccer.match_manager.common.enums.AcronymStatesEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ClubCreateRequest extends ClubRequest {
    public ClubCreateRequest(String name, AcronymStatesEnum stateAcronym, LocalDate creationDate, Boolean active) {
        super(name, stateAcronym, creationDate, active);
    }
}
