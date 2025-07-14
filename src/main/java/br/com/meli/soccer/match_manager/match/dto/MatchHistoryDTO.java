package br.com.meli.soccer.match_manager.match.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchHistoryDTO {
    private int totalVictories;
    private int totalDraws;
    private int totalDefeats;
    private int goalsScored;
    private int goalsConceded;
    private OpponentDTO adversary;

    public MatchHistoryDTO(OpponentDTO adversary) {
        this.setAdversary(adversary);
    }

    public void generateResult(int goalsScored, int goalsConceded) {
        this.goalsScored += goalsScored;
        this.goalsConceded += goalsConceded;

        if(goalsScored == goalsConceded) {
            totalDraws++;
        }

        if(goalsScored > goalsConceded) {
            totalVictories++;
        }

        if(goalsScored < goalsConceded) {
            totalDefeats++;
        }
    }
}
