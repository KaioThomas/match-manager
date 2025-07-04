package br.com.meli.soccer.match_manager.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record StadiumRequestDTO(
        @NotEmpty
        @Size(min = 2, max = 50)
        String name
) {
}
