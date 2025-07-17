package br.com.meli.soccer.match_manager.match.dto.response;

import br.com.meli.soccer.match_manager.match.dto.Opponent;
import lombok.*;

@ToString
@Getter
@Setter
public class RetrospectByOpponentResponse extends ClubTotalRetrospectResponse {

    private Opponent opponent;

    public RetrospectByOpponentResponse(Opponent opponent) {
        super();
        this.setOpponent(opponent);
    }
}
