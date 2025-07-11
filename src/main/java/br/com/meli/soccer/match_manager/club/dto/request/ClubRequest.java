package br.com.meli.soccer.match_manager.club.dto.request;

import java.time.LocalDate;

public interface ClubRequest {
    String name();
    String stateAcronym();
    LocalDate creationDate();
    Boolean active();
}