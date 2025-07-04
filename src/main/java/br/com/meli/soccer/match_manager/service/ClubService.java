package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.model.dto.request.ClubRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.ClubResponseDTO;

import java.util.List;

public interface ClubService {

    ClubResponseDTO create(ClubRequestDTO clubRequestDTO);

    ClubResponseDTO update(ClubRequestDTO clubRequestDTO, String id);

    ClubResponseDTO getById(String id);

    void deleteById(String id);

    List<ClubResponseDTO> getAll(
            String name,
            String acronymState,
            Boolean active,
            Integer pageNumber,
            Integer size,
            String orderBy,
            String direction
    );
}
