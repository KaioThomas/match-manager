package br.com.meli.soccer.match_manager.model.dto.response;

import java.time.LocalDate;

public record ClubResponseDTO(
        String id,
        String name,
        String acronymState,
        LocalDate creationDate,
        Boolean active
) { }
