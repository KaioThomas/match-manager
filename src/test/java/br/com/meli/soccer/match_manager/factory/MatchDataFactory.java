package br.com.meli.soccer.match_manager.factory;

import br.com.meli.soccer.match_manager.club.dto.request.ClubResult;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponse;
import br.com.meli.soccer.match_manager.match.service.MatchService;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class MatchDataFactory {

    private final MatchService matchService;

    public MatchResponse createMatch(ClubResponse homeClub, ClubResponse visitingClub, StadiumResponse stadium, LocalDateTime localDateTime) {
        ClubResult homeClubResult = new ClubResult(homeClub.id(), ThreadLocalRandom.current().nextInt(0, 20));
        ClubResult visitingClubResult = new ClubResult(visitingClub.id(), ThreadLocalRandom.current().nextInt(0, 20));

        return createMatchAtDatabase(homeClubResult, visitingClubResult, stadium.id(), localDateTime);
    }

    private MatchResponse createMatchAtDatabase(ClubResult homeClubResult, ClubResult visitingClubResult, String stadiumId, LocalDateTime dateTime) {
        MatchCreateRequest matchCreateRequest = new MatchCreateRequest(homeClubResult, visitingClubResult, stadiumId, dateTime);
        return matchService.create(matchCreateRequest);
    }
}
