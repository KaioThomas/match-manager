package br.com.meli.soccer.match_manager.stadium.service;

import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumCreateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumRequestDTO;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumUpdateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StadiumService {

    StadiumResponseDTO create(StadiumCreateRequest stadiumCreateRequest);

    StadiumResponseDTO update(StadiumUpdateRequest stadiumUpdateRequest);

    StadiumResponseDTO getById(String id);

    List<StadiumResponseDTO> getAll(String name, Pageable pageable);
}
