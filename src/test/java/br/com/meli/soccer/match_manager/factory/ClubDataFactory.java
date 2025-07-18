package br.com.meli.soccer.match_manager.factory;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.club.service.ClubService;
import br.com.meli.soccer.match_manager.common.enums.AcronymStatesEnum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class ClubDataFactory {

    private final ClubService clubService;

    public ClubResponse createGremio() {
        return createClubAtDatabase(
                "grêmio",
                AcronymStatesEnum.GO,
                LocalDate.of(1978, 3, 12),
                true
        );
    }

    public ClubResponse createGremio2() {
        return createClubAtDatabase(
                "grêmio",
                AcronymStatesEnum.SP,
                LocalDate.of(1978, 3, 12),
                true
        );
    }

    public ClubResponse createAtletico() {
        return createClubAtDatabase(
                "atlético",
                AcronymStatesEnum.AM, LocalDate.of(1964, 8, 25),
                true
        );
    }

    public ClubResponse createUniaoLitoranea() {
        return createClubAtDatabase(
                "união litorânea",
                AcronymStatesEnum.SC,
                LocalDate.of(1983, 11, 10),
                false
        );
    }

    public ClubResponse createFenixMetropolitana() {
        return createClubAtDatabase(
                "fênix metropolitana",
                AcronymStatesEnum.SP,
                LocalDate.of(2002, 5, 5),
                true
        );
    }

    public ClubResponse createClubAtDatabase(String name, AcronymStatesEnum acronymState, LocalDate localDate, Boolean active) {
        ClubCreateRequest clubCreateRequest = new ClubCreateRequest(name, acronymState, localDate, active);
        return clubService.create(clubCreateRequest);
    }
}
