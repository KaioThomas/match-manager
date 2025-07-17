package br.com.meli.soccer.match_manager.match.dto.response;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubTotalRetrospectResponse {
    private int totalVictories;
    private int totalDraws;
    private int totalDefeats;
    private int goalsScored;
    private int goalsConceded;

    public void generateResult(int goalsScored, int goalsConceded) {
        this.setGoalsScored(this.getGoalsScored() + goalsScored);
        this.setGoalsConceded(this.getGoalsConceded() + goalsConceded);

        if(goalsScored == goalsConceded) {
            this.setTotalDraws(this.getTotalDraws() + 1);
        }

        if(goalsScored > goalsConceded) {
            this.setTotalVictories(this.getTotalVictories() + 1);
        }

        if(goalsScored < goalsConceded) {
            this.setTotalDefeats(this.getTotalDefeats() + 1);
        }
    }
}
