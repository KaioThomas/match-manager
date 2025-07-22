package br.com.meli.soccer.match_manager.match.dto.response;

import br.com.meli.soccer.match_manager.match.dto.ClubData;
import br.com.meli.soccer.match_manager.match.dto.GeneralRetrospect;
import lombok.*;

@ToString
@Getter
@Setter
public class RetrospectByOpponent extends GeneralRetrospect {

    private ClubData opponent;

    public RetrospectByOpponent(ClubData opponent) {
        super();
        this.setOpponent(opponent);
    }
}
