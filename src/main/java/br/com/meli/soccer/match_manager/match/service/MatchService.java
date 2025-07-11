package br.com.meli.soccer.match_manager.match.service;

import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchFilterRequestDTO;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchService {

    MatchResponseDTO create(MatchCreateRequest matchCreateRequest);

    MatchResponseDTO update(MatchUpdateRequest matchUpdateRequest);

    MatchResponseDTO getById(String id);

    List<MatchResponseDTO> getAll(MatchFilterRequestDTO matchFilterRequestDTO, Pageable pageable);

    void deleteById(String id);
}
