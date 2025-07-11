package br.com.meli.soccer.match_manager.club.service;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.request.ClubUpdateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubService {

    ClubResponseDTO create(ClubCreateRequest clubCreateRequest);

    ClubResponseDTO update(ClubUpdateRequest clubUpdateRequest);

    ClubResponseDTO getById(String id);

    void deleteById(String id);

    List<ClubResponseDTO> getAll(
            String name,
            Boolean active,
            String acronymState,
            Pageable pageable
    );
}
