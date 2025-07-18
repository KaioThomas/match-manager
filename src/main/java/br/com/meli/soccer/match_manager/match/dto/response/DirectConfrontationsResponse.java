package br.com.meli.soccer.match_manager.match.dto.response;

import br.com.meli.soccer.match_manager.match.dto.ClubSummary;
import java.util.List;

public record DirectConfrontationsResponse(
        ClubSummary clubA,

        ClubSummary clubB,

        List<MatchResponse> matches
) { }
