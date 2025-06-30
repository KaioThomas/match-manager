package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.enums.ExceptionsEnum;
import br.com.meli.soccer.match_manager.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.dto.request.ClubRequestDTO;
import br.com.meli.soccer.match_manager.enums.AcronymStatesEnum;
import br.com.meli.soccer.match_manager.exception.NotFoundException;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public Club createClub(ClubRequestDTO clubRequestDTO) {

        Club club = createClubFromClubRequestDTO(clubRequestDTO);

        throwIfClubExists(club);
        throwIfInvalidClubRequestFields(club);

        return this.clubRepository.save(club);
    }

    @Override
    public Club updateClub(ClubRequestDTO clubRequestDTO) {
        Club club = createClubFromClubRequestDTO(clubRequestDTO);
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

    private void throwIfInvalidClubRequestFields(Club club) {
        if(!AcronymStatesEnum.isValidAcronym(club.getAcronymState())) {
            throw new InvalidFieldsException(ExceptionsEnum.NON_EXISTENT_ACRONYM_STATE.getValue());
        }
    }

    @Override
    public void delete(Long id) {
        Club club = this.getClub(id);
        club.setActive(false);
        this.clubRepository.save(club);
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

    private Club createClubFromClubRequestDTO(ClubRequestDTO clubRequestDTO) {
        Club club = new Club();
        club.setId(clubRequestDTO.getId());
        club.setName(clubRequestDTO.getName().toUpperCase());
        club.setActive(clubRequestDTO.getActive());
        club.setCreationDate(clubRequestDTO.getCreationDate());
        club.setAcronymState(clubRequestDTO.getAcronymState().toUpperCase());
        return club;
    }
}
