package br.com.meli.soccer.match_manager.model.dto.response.club;

import java.time.LocalDate;
import java.util.UUID;

public record ClubResponseDTO(
        UUID id,
        String name,
        String acronymState,
        LocalDate creationDate,
        Boolean active
) { }
