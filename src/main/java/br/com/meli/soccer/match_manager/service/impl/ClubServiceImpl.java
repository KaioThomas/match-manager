package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.enums.ExceptionsEnum;
import br.com.meli.soccer.match_manager.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.dto.request.ClubRequestDTO;
import br.com.meli.soccer.match_manager.enums.AcronymStatesEnum;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        Club club = new Club();
        club.setName(clubRequestDTO.getName());
        club.setActive(clubRequestDTO.isActive());
        club.setCreationDate(clubRequestDTO.getCreationDate());
        club.setAcronymState(clubRequestDTO.getAcronymState().toUpperCase());

        throwIfClubExists(club);
        throwIfClubRequestFields(club);

        return this.clubRepository.save(club);
    }

    private void throwIfClubRequestFields(Club club) {
        if(!AcronymStatesEnum.isValidAcronym(club.getAcronymState())) {
            throw new InvalidFieldsException(ExceptionsEnum.NON_EXISTENT_ACRONYM_STATE.getValue());
        }

        if(club.getCreationDate().isAfter(LocalDate.now())) {
            throw new InvalidFieldsException(ExceptionsEnum.CREATION_DATE_IN_FUTURE.getValue());
        }
    }

    private void throwIfClubExists(Club club) {
        List<Club> clubs = this.clubRepository.findAllByName(club.getName());

        boolean alreadyHasClub = clubs.stream()
                .anyMatch(
                        (item) -> club.getName().equalsIgnoreCase(item.getName()) &&
                                       club.getAcronymState().equalsIgnoreCase(item.getAcronymState())
                );
        if(alreadyHasClub) {
            throw new CreationConflictException(ExceptionsEnum.CLUB_EXISTS.getValue());
        }
    }
}
