package br.com.meli.soccer.match_manager.factory;

import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumCreateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import br.com.meli.soccer.match_manager.stadium.service.StadiumService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StadiumDataFactory {

    private final StadiumService stadiumService;

    public StadiumResponse createArenaPampa() {
        return createStadiumAtDatabase("arena do pampa");
    }

    public StadiumResponse createHorizonteAzul() {
        return createStadiumAtDatabase("horizonte azul");
    }

    public StadiumResponse createSolarDasPalmeiras() {
        return createStadiumAtDatabase("solar das palmeiras");
    }

    private StadiumResponse createStadiumAtDatabase(String name) {
        StadiumCreateRequest stadiumCreateRequest = new StadiumCreateRequest(name);
        return stadiumService.create(stadiumCreateRequest);
    }
}
