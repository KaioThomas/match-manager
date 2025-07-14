package br.com.meli.soccer.match_manager.match.dto.response;

import br.com.meli.soccer.match_manager.match.dto.MatchHistoryDTO;

import java.util.List;

public record MatchHistoryResponse(
        List<MatchHistoryDTO> matchHistory
) { }
