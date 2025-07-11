package br.com.meli.soccer.match_manager.match.dto.response;

import br.com.meli.soccer.match_manager.club.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponseDTO;

import java.time.LocalDateTime;

public record MatchResponseDTO(
        String id,
        ClubResponseDTO homeClub,
        ClubResponseDTO visitingClub,
        Integer homeClubGoals,
        Integer visitingClubGoals,
        StadiumResponseDTO stadium,
        LocalDateTime dateTime
) { }
