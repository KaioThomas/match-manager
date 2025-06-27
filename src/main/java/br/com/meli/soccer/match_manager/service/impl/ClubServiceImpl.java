package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.dto.ClubDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public Club createClub(ClubDTO clubDTO) {
        Club club = new Club();
        club.setName(clubDTO.getName());
        club.setActive(clubDTO.isActive());
        club.setCreationDate(clubDTO.getCreationDate());
        club.setAcronymState(clubDTO.getAcronymState());
        return this.clubRepository.save(club);
    }
}
