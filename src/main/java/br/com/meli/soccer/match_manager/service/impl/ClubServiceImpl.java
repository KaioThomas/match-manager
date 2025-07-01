package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.exception.NotFoundException;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.service.ClubService;
import br.com.meli.soccer.match_manager.service.ClubValidator;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final ClubValidator clubValidator;

    public ClubServiceImpl(ClubRepository clubRepository, ClubValidator clubValidator) {
        this.clubRepository = clubRepository;
        this.clubValidator = clubValidator;
    }

    @Override
    public Club createClub(ClubCreateRequestDTO clubCreateRequestDTO) {

        Club club = new Club();
        club.setActive(clubCreateRequestDTO.active());
        club.setName(clubCreateRequestDTO.name().toUpperCase());
        club.setCreationDate(clubCreateRequestDTO.creationDate());
        club.setAcronymState(clubCreateRequestDTO.acronymState().toUpperCase());

        this.clubValidator.validate(club);

        return this.clubRepository.save(club);
    }

    @Override
    public Club updateClub(ClubUpdateRequestDTO clubUpdateRequestDTO) {

        Club club = new Club();
        club.setId(clubUpdateRequestDTO.id());
        club.setActive(clubUpdateRequestDTO.active());
        club.setName(clubUpdateRequestDTO.name().toUpperCase());
        club.setCreationDate(clubUpdateRequestDTO.creationDate());
        club.setAcronymState(clubUpdateRequestDTO.acronymState().toUpperCase());

        this.clubValidator.validate(club);

        boolean clubExists = this.clubRepository.existsById(club.getId());

        if(!clubExists) {
            throw new NotFoundException("You can't update a club that doesn't exist");
        }

        return this.clubRepository.save(club);
    }

    @Override
    public Club getClub(Long id) {
        return this.clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club not found"));
    }

    @Override
    public void delete(Long id) {
        Club club = this.getClub(id);
        club.setActive(false);
        this.clubRepository.save(club);
    }
}
