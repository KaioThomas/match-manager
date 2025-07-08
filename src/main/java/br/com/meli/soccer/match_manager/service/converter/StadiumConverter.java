package br.com.meli.soccer.match_manager.service.converter;

import br.com.meli.soccer.match_manager.model.dto.request.StadiumRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.model.entity.Stadium;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StadiumConverter {

    public static Stadium toEntity(StadiumRequestDTO stadiumRequestDTO) {

        Stadium stadium = new Stadium();
        stadium.setName(stadiumRequestDTO.name().toUpperCase());
        return stadium;
    }

    public static StadiumResponseDTO toResponseDTO(Stadium stadium) {
        return new StadiumResponseDTO(stadium.getId(), stadium.getName());
    }
}
