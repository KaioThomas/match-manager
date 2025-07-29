package br.com.meli.soccer.match_manager.match.dto;

import java.math.BigDecimal;

public record Ranking(
        String id,
        String name,
        BigDecimal totalScore,
        BigDecimal totalVictories,
        BigDecimal totalGoals,
        Long totalGames
) { }
