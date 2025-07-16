package br.com.meli.soccer.match_manager.match.dto.filter;

public record MatchFilterRequestDTO(
        String homeClubId,
        String stadiumId,
        String visitingClubId
) { }
