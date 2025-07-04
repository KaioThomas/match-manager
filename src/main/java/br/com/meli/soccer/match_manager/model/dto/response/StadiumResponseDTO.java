package br.com.meli.soccer.match_manager.model.dto.response;

import java.util.UUID;

public record StadiumResponseDTO (
    UUID id,
    String name
) { }