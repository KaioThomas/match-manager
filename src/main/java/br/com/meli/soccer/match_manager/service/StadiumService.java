package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.model.dto.request.StadiumRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.StadiumResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StadiumService {

    StadiumResponseDTO create(StadiumRequestDTO stadiumRequestDTO);

    StadiumResponseDTO update(StadiumRequestDTO stadiumRequestDTO, String id);

    StadiumResponseDTO getById(String id);

    List<StadiumResponseDTO> getAll(String name, Pageable pageable);
}
