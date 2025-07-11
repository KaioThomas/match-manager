package br.com.meli.soccer.match_manager.match.mapper;

import br.com.meli.soccer.match_manager.match.dto.request.MatchRequestDTO;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.club.entity.Club;
import br.com.meli.soccer.match_manager.match.entity.Match;
import br.com.meli.soccer.match_manager.stadium.entity.Stadium;
import br.com.meli.soccer.match_manager.club.mapper.ClubMapper;
import br.com.meli.soccer.match_manager.stadium.mapper.StadiumMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchMapper {

    public static Match toEntity(MatchRequestDTO matchRequestDTO, Stadium stadium, Club homeClub, Club visitingClub, Match match) {
        return match.toBuilder()
                .homeClub(homeClub)
                .homeClubGoals(matchRequestDTO.homeClubResult().goals())
                .visitingClub(visitingClub)
                .visitingClubGoals(matchRequestDTO.visitingClubResult().goals())
                .stadium(stadium)
                .dateTime(matchRequestDTO.dateTime())
                .build();
    }

    public static MatchResponseDTO toResponseDTO(Match match) {
        ClubResponseDTO homeClubResponse = ClubMapper.toResponse(match.getHomeClub());
        ClubResponseDTO visitingClubResponse = ClubMapper.toResponse(match.getVisitingClub());
        StadiumResponseDTO stadiumResponseDTO = StadiumMapper.toResponseDTO(match.getStadium());

        return new MatchResponseDTO(
                match.getId(),
                homeClubResponse,
                visitingClubResponse,
                match.getHomeClubGoals(),
                match.getVisitingClubGoals(),
                stadiumResponseDTO,
                match.getDateTime()
        );
    }
}
