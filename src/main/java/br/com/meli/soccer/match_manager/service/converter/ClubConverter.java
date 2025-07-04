package br.com.meli.soccer.match_manager.service.converter;

import br.com.meli.soccer.match_manager.model.dto.request.ClubRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import org.springframework.stereotype.Service;

@Service
public class ClubConverter {

    private ClubConverter() {}

    public static Club toEntity(ClubRequestDTO clubRequestDTO) {
        Club club = new Club();
        club.setActive(clubRequestDTO.active());
        club.setName(clubRequestDTO.name().toUpperCase());
        club.setCreationDate(clubRequestDTO.creationDate());
        club.setStateAcronym(clubRequestDTO.acronymState().toUpperCase());
        return club;
    }

    public static ClubResponseDTO toResponseDTO(Club club) {
        return new ClubResponseDTO(
                club.getId(),
                club.getName(),
                club.getStateAcronym(),
                club.getCreationDate(),
                club.getActive());
    }
}
