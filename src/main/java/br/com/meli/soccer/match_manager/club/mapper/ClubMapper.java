package br.com.meli.soccer.match_manager.club.mapper;

import br.com.meli.soccer.match_manager.club.dto.request.ClubRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.club.entity.Club;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClubMapper {

    public static Club toEntity(ClubRequest clubRequest, Club club) {
        return club.toBuilder()
                .name(clubRequest.getName().toUpperCase())
                .creationDate(clubRequest.getCreationDate())
                .stateAcronym(clubRequest.getStateAcronym().toString())
                .active(clubRequest.getActive())
                .build();
    }

    public static ClubResponse toResponse(Club club) {
        return new ClubResponse(
                club.getId(),
                club.getName(),
                club.getStateAcronym(),
                club.getCreationDate(),
                club.getActive()
        );
    }
}
