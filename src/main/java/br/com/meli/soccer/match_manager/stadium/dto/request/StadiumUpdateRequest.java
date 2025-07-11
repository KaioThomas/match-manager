package br.com.meli.soccer.match_manager.stadium.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record StadiumUpdateRequest(
        @NotEmpty
        String id,

        @NotEmpty
        @Size(min = 3, max = 50)
        String name
) implements StadiumRequestDTO { }
