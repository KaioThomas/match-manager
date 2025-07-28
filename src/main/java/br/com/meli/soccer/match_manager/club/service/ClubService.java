package br.com.meli.soccer.match_manager.club.service;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.request.ClubUpdateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubService {

    ClubResponse create(ClubCreateRequest clubCreateRequest);

    ClubResponse update(ClubUpdateRequest clubUpdateRequest);

    ClubResponse getById(String id);

    void deleteById(String id);

    Page<ClubResponse> getAll(
            String name,
            Boolean active,
            String acronymState,
            Pageable pageable
    );
}
