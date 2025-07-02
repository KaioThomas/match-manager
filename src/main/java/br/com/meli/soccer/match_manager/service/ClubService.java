package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.club.ClubResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ClubService {

    ClubResponseDTO create(ClubCreateRequestDTO clubRequestDTO);

    ClubResponseDTO update(ClubUpdateRequestDTO clubRequestDTO);

    ClubResponseDTO getById(UUID id);

    void deleteById(UUID id);

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
