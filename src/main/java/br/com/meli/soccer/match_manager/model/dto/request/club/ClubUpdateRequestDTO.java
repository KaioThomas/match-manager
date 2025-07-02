package br.com.meli.soccer.match_manager.model.dto.request.club;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record ClubUpdateRequestDTO(
        @NotNull
        UUID id,

        @NotEmpty
        @Size(min = 2)
        String name,

        @NotEmpty
        @Size(min = 2, max = 2)
        String acronymState,

        @NotNull
        @PastOrPresent
        LocalDate creationDate,

        @NotNull
        Boolean active
) { }
