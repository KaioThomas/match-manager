package br.com.meli.soccer.match_manager.stadium.mapper;

import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import br.com.meli.soccer.match_manager.stadium.entity.Stadium;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StadiumMapper {

    public static Stadium toEntity(StadiumRequest stadiumRequest, Stadium stadium) {

        return stadium.toBuilder()
                .name(stadiumRequest.getName().toUpperCase())
                .build();
    }

    public static StadiumResponse toResponseDTO(Stadium stadium) {
        return new StadiumResponse(stadium.getId(), stadium.getName());
    }
}
