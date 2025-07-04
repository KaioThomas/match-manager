package br.com.meli.soccer.match_manager.service.converter;

import br.com.meli.soccer.match_manager.model.dto.request.MatchRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.model.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.model.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.model.entity.Match;
import br.com.meli.soccer.match_manager.model.entity.Stadium;

import java.util.Optional;
import java.util.UUID;

public class MatchConverter {

    public static Match toEntity(MatchRequestDTO matchRequestDTO, Stadium stadium, Club homeClub, Club visitingclub) {
        Match match = new Match();

        match.setHomeClub(homeClub);
        match.setVisitingClub(visitingclub);
        match.setStadium(stadium);
        match.setHomeClubGoals(match.getHomeClubGoals());
        match.setVisitingClubGoals(match.getVisitingClubGoals());
        match.setDateAndHour(matchRequestDTO.dateAndHour());
        return match;
    }

    public static Match toEntity(MatchRequestDTO matchRequestDTO, Stadium stadium, Club homeClub, Club visitingclub, String id) {
        Match match = new Match();

        match.setId(UUID.fromString(id));
        match.setHomeClub(homeClub);
        match.setVisitingClub(visitingclub);
        match.setStadium(stadium);
        match.setHomeClubGoals(match.getHomeClubGoals());
        match.setVisitingClubGoals(match.getVisitingClubGoals());
        match.setDateAndHour(matchRequestDTO.dateAndHour());
        return match;
    }

    public static MatchResponseDTO toResponseDTO(Match match) {
        ClubResponseDTO homeClubResponse = ClubConverter.toResponseDTO(match.getHomeClub());
        ClubResponseDTO visitingClubResponse = ClubConverter.toResponseDTO(match.getVisitingClub());
        StadiumResponseDTO stadiumResponseDTO = StadiumConverter.toResponseDTO(match.getStadium());

        return new MatchResponseDTO(
                match.getId().toString(),
                homeClubResponse,
                visitingClubResponse,
                match.getHomeClubGoals(),
                match.getVisitingClubGoals(),
                stadiumResponseDTO,
                match.getDateAndHour()
        );
    }
}
