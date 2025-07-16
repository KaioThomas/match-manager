package br.com.meli.soccer.match_manager.factory;

import br.com.meli.soccer.match_manager.club.dto.request.ClubResultDTO;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.match.service.MatchService;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponseDTO;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class MatchDataFactory {

    private final MatchService matchService;

    public MatchResponseDTO createMatch(ClubResponseDTO homeClub, ClubResponseDTO visitingClub, StadiumResponseDTO stadium, LocalDateTime localDateTime) {
        ClubResultDTO homeClubResult = new ClubResultDTO(homeClub.id(), ThreadLocalRandom.current().nextInt(0, 20));
        ClubResultDTO visitingClubResult = new ClubResultDTO(visitingClub.id(), ThreadLocalRandom.current().nextInt(0, 20));

        return createMatchAtDatabase(homeClubResult, visitingClubResult, stadium.id(), localDateTime);
    }

    private MatchResponseDTO createMatchAtDatabase(ClubResultDTO homeClubResult, ClubResultDTO visitingClubResult, String stadiumId, LocalDateTime dateTime) {
        MatchCreateRequest matchCreateRequest = new MatchCreateRequest(homeClubResult, visitingClubResult, stadiumId, dateTime);
        return matchService.create(matchCreateRequest);
    }
}
