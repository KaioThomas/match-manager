package br.com.meli.soccer.match_manager.match.dto;

import lombok.*;

@ToString
@Getter
@Setter
public class MatchHistoryDTO extends MatchTotalRetrospect{

    private OpponentDTO adversary;

    public MatchHistoryDTO(OpponentDTO adversary) {
        super();
        this.setAdversary(adversary);
    }
}
