package br.com.meli.soccer.match_manager.stadium.mapper;

import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumRequestDTO;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.stadium.entity.Stadium;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StadiumMapper {

    public static Stadium toEntity(StadiumRequestDTO stadiumRequestDTO, Stadium stadium) {

        return stadium.toBuilder()
                .name(stadiumRequestDTO.name().toUpperCase())
                .build();
    }

    public static StadiumResponseDTO toResponseDTO(Stadium stadium) {
        return new StadiumResponseDTO(stadium.getId(), stadium.getName());
    }
}
