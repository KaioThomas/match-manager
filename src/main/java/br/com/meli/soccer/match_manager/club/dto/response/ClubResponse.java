package br.com.meli.soccer.match_manager.club.dto.response;

import java.time.LocalDate;

public record ClubResponse(
        String id,
        String name,
        String acronymState,
        LocalDate creationDate,
        Boolean active
) { }
