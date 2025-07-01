package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import org.springframework.stereotype.Service;

@Service
public class ClubConverter {

    private ClubConverter() {}

    public static Club toEntity(ClubCreateRequestDTO clubCreateRequestDTO) {
        Club club = new Club();
        club.setActive(clubCreateRequestDTO.active());
        club.setName(clubCreateRequestDTO.name().toUpperCase());
        club.setCreationDate(clubCreateRequestDTO.creationDate());
        club.setAcronymState(clubCreateRequestDTO.acronymState().toUpperCase());
        return club;
    }

    public static Club toEntity(ClubUpdateRequestDTO clubUpdateRequestDTO) {
        Club club = new Club();
        club.setId(clubUpdateRequestDTO.id());
        club.setActive(clubUpdateRequestDTO.active());
        club.setName(clubUpdateRequestDTO.name().toUpperCase());
        club.setCreationDate(clubUpdateRequestDTO.creationDate());
        club.setAcronymState(clubUpdateRequestDTO.acronymState().toUpperCase());
        return club;
    }
}
