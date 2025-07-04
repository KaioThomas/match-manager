package br.com.meli.soccer.match_manager.model.dto.response;

import java.time.LocalDateTime;

public record MatchResponseDTO(
        String id,
        ClubResponseDTO homeClub,
        ClubResponseDTO visitingClub,
        Integer homeClubGoals,
        Integer visitingClubGoals,
        StadiumResponseDTO stadium,
        LocalDateTime dateAndHour
) { }
