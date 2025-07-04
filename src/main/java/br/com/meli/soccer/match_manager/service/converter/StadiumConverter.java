package br.com.meli.soccer.match_manager.service.converter;

import br.com.meli.soccer.match_manager.model.dto.request.StadiumRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.model.entity.Stadium;

import java.util.Optional;
import java.util.UUID;

public class StadiumConverter {


    public static Stadium toEntity(StadiumRequestDTO stadiumRequestDTO, Optional<String> id) {

        Stadium stadium = new Stadium();

        id.ifPresent(idOrNull -> stadium.setId(UUID.fromString(idOrNull)));

        stadium.setName(stadiumRequestDTO.name());
        return stadium;
    }

    public static StadiumResponseDTO toResponseDTO(Stadium stadium) {
        return new StadiumResponseDTO(stadium.getId(), stadium.getName());
    }
}
