package br.com.meli.soccer.match_manager.stadium.service;

import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumCreateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumUpdateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StadiumService {

    StadiumResponse create(StadiumCreateRequest stadiumCreateRequest);

    StadiumResponse update(StadiumUpdateRequest stadiumUpdateRequest);

    StadiumResponse getById(String id);

    List<StadiumResponse> getAll(String name, Pageable pageable);
}
