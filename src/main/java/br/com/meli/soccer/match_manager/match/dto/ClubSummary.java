package br.com.meli.soccer.match_manager.match.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClubSummary extends GeneralRetrospect {

    private String id;
    private String name;

    public ClubSummary(String id, String name, int totalVictories, int totalDraws, int totalDefeats, int goalsScored, int goalsConceded) {
        super(totalVictories, totalDraws, totalDefeats, goalsScored, goalsConceded);
        this.id = id;
        this.name = name;
    }

    public ClubSummary() {
       super();
    }
}
