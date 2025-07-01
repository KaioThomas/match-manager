package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;

import java.util.UUID;

public interface ClubService {

    Club create(ClubCreateRequestDTO clubRequestDTO);

    Club update(ClubUpdateRequestDTO clubRequestDTO);

    Club getById(UUID id);

    void deleteById(UUID id);

}
