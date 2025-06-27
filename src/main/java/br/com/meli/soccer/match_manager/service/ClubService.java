package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.dto.ClubDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;

public interface ClubService {

    Club createClub(ClubDTO clubDTO);
}
