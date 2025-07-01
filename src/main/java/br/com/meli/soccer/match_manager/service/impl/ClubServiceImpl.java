package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.enums.ExceptionsEnum;
import br.com.meli.soccer.match_manager.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.enums.AcronymStatesEnum;
import br.com.meli.soccer.match_manager.exception.NotFoundException;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.service.ClubService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }


    @Override
    public Club createClub(ClubCreateRequestDTO clubCreateRequestDTO) {

        Club club = new Club();
        club.setActive(clubCreateRequestDTO.active());
        club.setName(clubCreateRequestDTO.name().toUpperCase());
        club.setCreationDate(clubCreateRequestDTO.creationDate());
        club.setAcronymState(clubCreateRequestDTO.acronymState().toUpperCase());

        throwIfClubExists(club);
        throwIfInvalidClubRequestFields(club);

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

        throwIfClubExists(club);
        throwIfInvalidClubRequestFields(club);

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

    private void throwIfInvalidClubRequestFields(Club club) {
        if(!AcronymStatesEnum.isValidAcronym(club.getAcronymState())) {
            throw new InvalidFieldsException(ExceptionsEnum.NON_EXISTENT_ACRONYM_STATE.getValue());
        }
    }

    private void throwIfClubExists(Club club) {
        List<Club> clubs = this.clubRepository.findAllByName(club.getName());

        boolean alreadyHasClub = clubs.stream()
                .anyMatch(
                        item -> club.getName().equalsIgnoreCase(item.getName()) &&
                                      club.getAcronymState().equalsIgnoreCase(item.getAcronymState()) &&
                                     !club.getId().equals(item.getId())
                );
        if(alreadyHasClub) {
            throw new CreationConflictException(ExceptionsEnum.CLUB_EXISTS.getValue());
        }
    }

}
