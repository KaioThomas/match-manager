package br.com.meli.soccer.match_manager.match.dto.response;

import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;

import java.time.LocalDateTime;

public record MatchResponse(
        String id,
        ClubResponse homeClub,
        ClubResponse visitingClub,
        Integer homeClubGoals,
        Integer visitingClubGoals,
        StadiumResponse stadium,
        LocalDateTime dateTime
) { }
