package br.com.meli.soccer.match_manager.match.dto.request;

import br.com.meli.soccer.match_manager.club.dto.request.ClubResult;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class MatchCreateRequest extends MatchRequest {

    public MatchCreateRequest(ClubResult homeClubResult, ClubResult visitingClubResult, String stadiumId, LocalDateTime dateTime) {
        super(homeClubResult, visitingClubResult, stadiumId, dateTime);
    }
}
