package br.com.meli.soccer.match_manager.match.dto.request;

import br.com.meli.soccer.match_manager.club.dto.request.ClubResultDTO;

import java.time.LocalDateTime;

public interface MatchRequest {
        ClubResultDTO homeClubResult();
        ClubResultDTO visitingClubResult();
        String stadiumId();
        LocalDateTime dateTime();
}
