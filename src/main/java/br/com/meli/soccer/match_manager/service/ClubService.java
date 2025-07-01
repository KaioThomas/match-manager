package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;

public interface ClubService {

    Club createClub(ClubCreateRequestDTO clubRequestDTO);

    Club updateClub(ClubUpdateRequestDTO clubRequestDTO);

    Club getClub(Long id);

    void delete(Long id);

}
