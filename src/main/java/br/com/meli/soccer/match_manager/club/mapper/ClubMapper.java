package br.com.meli.soccer.match_manager.club.mapper;

import br.com.meli.soccer.match_manager.club.dto.request.ClubRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.club.entity.Club;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClubMapper {

    public static Club toEntity(ClubRequest clubRequest, Club club) {
        return club.toBuilder()
                .name(clubRequest.name().toUpperCase())
                .creationDate(clubRequest.creationDate())
                .stateAcronym(clubRequest.stateAcronym().toUpperCase())
                .active(clubRequest.active())
                .build();
    }

    public static ClubResponseDTO toResponse(Club club) {
        return new ClubResponseDTO(
                club.getId(),
                club.getName(),
                club.getStateAcronym(),
                club.getCreationDate(),
                club.getActive()
        );
    }
}
