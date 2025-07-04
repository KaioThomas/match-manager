package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.model.dto.request.MatchRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.MatchResponseDTO;

import java.util.List;
import java.util.UUID;

public interface MatchService {

    MatchResponseDTO create(MatchRequestDTO matchRequestDTO);

    MatchResponseDTO update(MatchRequestDTO matchRequestDTO);

    MatchResponseDTO getById(UUID id);

    List<MatchResponseDTO> getAll(
            UUID clubId,
            UUID stadiumId,
            Integer pageNumber,
            Integer size,
            String orderBy,
            String direction
    );

    void deleteById(UUID id);
}
