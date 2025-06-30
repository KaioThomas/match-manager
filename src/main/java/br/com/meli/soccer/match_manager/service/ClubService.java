package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.dto.request.ClubRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;

public interface ClubService {

    Club createClub(ClubRequestDTO clubRequestDTO);
}
