package br.com.meli.soccer.match_manager.match.dto.response;

import br.com.meli.soccer.match_manager.match.dto.ClubData;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RankingResponse {

    private ClubData club;

    private int totalScore;

    private int totalGoals;

    private int totalVictories;

    private int totalGames;

    public RankingResponse(ClubData club) {
        this.club = club;
    }

    public void generateRanking(int totalVictories, int totalDraws, int totalScored, int totalGames) {
        this.totalVictories = totalVictories;
        this.totalScore = totalVictories > 0 ? this.totalScore + totalVictories * 3 : this.totalScore;
        this.totalScore = totalDraws > 0 ? this.totalScore + totalDraws : this.totalScore;
        this.totalGoals = totalScored;
        this.totalGames = totalGames;
    }
}
