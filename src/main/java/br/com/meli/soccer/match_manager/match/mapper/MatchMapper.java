package br.com.meli.soccer.match_manager.match.mapper;

import br.com.meli.soccer.match_manager.match.dto.request.MatchRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponse;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import br.com.meli.soccer.match_manager.club.entity.Club;
import br.com.meli.soccer.match_manager.match.entity.Match;
import br.com.meli.soccer.match_manager.stadium.entity.Stadium;
import br.com.meli.soccer.match_manager.club.mapper.ClubMapper;
import br.com.meli.soccer.match_manager.stadium.mapper.StadiumMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchMapper {

    public static Match toEntity(MatchRequest matchRequest, Stadium stadium, Club homeClub, Club visitingClub, Match match) {
        return match.toBuilder()
                .homeClub(homeClub)
                .homeClubGoals(matchRequest.getHomeClubResult().goals())
                .visitingClub(visitingClub)
                .visitingClubGoals(matchRequest.getVisitingClubResult().goals())
                .stadium(stadium)
                .dateTime(matchRequest.getDateTime())
                .build();
    }

    public static MatchResponse toResponseDTO(Match match) {
        ClubResponse homeClubResponse = ClubMapper.toResponse(match.getHomeClub());
        ClubResponse visitingClubResponse = ClubMapper.toResponse(match.getVisitingClub());
        StadiumResponse stadiumResponse = StadiumMapper.toResponseDTO(match.getStadium());

        return new MatchResponse(
                match.getId(),
                homeClubResponse,
                visitingClubResponse,
                match.getHomeClubGoals(),
                match.getVisitingClubGoals(),
                stadiumResponse,
                match.getDateTime()
        );
    }
}
