package br.com.meli.soccer.match_manager.service;

import br.com.meli.soccer.match_manager.enums.AcronymStatesEnum;
import br.com.meli.soccer.match_manager.enums.ExceptionsEnum;
import br.com.meli.soccer.match_manager.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubValidator {

    private final ClubRepository clubRepository;

    public void validate(Club club) {
        throwIfInvalidClubRequestFields(club);
        throwIfClubExists(club);
    }

    private void throwIfInvalidClubRequestFields(Club club) {
        if(!AcronymStatesEnum.isValidAcronym(club.getAcronymState())) {
            throw new InvalidFieldsException(ExceptionsEnum.NON_EXISTENT_ACRONYM_STATE.getValue());
        }
    }

    private void throwIfClubExists(Club club) {
        List<Club> clubs = clubRepository.findAllByName(club.getName());

        boolean isClubUpdate = club.getId() != null;
        boolean alreadyHasClub = clubs.stream()
                .anyMatch(
                        item -> club.getName().equalsIgnoreCase(item.getName()) &&
                                club.getAcronymState().equalsIgnoreCase(item.getAcronymState()) &&
                                !(isClubUpdate && club.getId().equals(item.getId()))
                );
        if(alreadyHasClub) {
            throw new CreationConflictException(ExceptionsEnum.CLUB_EXISTS.getValue());
        }
    }
}
