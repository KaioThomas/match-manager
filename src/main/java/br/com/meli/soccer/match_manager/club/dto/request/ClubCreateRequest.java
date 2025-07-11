package br.com.meli.soccer.match_manager.club.dto.request;

import br.com.meli.soccer.match_manager.club.validation.ValidClubName;
import br.com.meli.soccer.match_manager.common.validation.ValidStateAcronym;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ClubCreateRequest(
    @ValidClubName
    String name,

    @ValidStateAcronym
    String stateAcronym,

    @NotNull
    @PastOrPresent
    LocalDate creationDate,

    @NotNull
    Boolean active
) implements ClubRequest { }
