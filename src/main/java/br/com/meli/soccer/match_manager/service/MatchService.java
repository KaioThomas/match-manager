package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.model.dto.request.MatchRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.filter.MatchFilterRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.MatchResponseDTO;

import java.util.List;

public interface MatchService {

    MatchResponseDTO create(MatchRequestDTO matchRequestDTO);

    MatchResponseDTO update(MatchRequestDTO matchRequestDTO);

    MatchResponseDTO getById(String id);

    List<MatchResponseDTO> getAll(MatchFilterRequestDTO matchFilterRequestDTO);

    void deleteById(String id);
}
