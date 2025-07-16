package br.com.meli.soccer.match_manager.factory;

import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumCreateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.stadium.service.StadiumService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StadiumDataFactory {

    private final StadiumService stadiumService;

    public StadiumResponseDTO createArenaPampa() {
        return createStadiumAtDatabase("arena do pampa");
    }

    public StadiumResponseDTO createHorizonteAzul() {
        return createStadiumAtDatabase("horizonte azul");
    }

    public StadiumResponseDTO createSolarDasPalmeiras() {
        return createStadiumAtDatabase("solar das palmeiras");
    }

    private StadiumResponseDTO createStadiumAtDatabase(String name) {
        StadiumCreateRequest stadiumCreateRequest = new StadiumCreateRequest(name);
        return stadiumService.create(stadiumCreateRequest);
    }
}
