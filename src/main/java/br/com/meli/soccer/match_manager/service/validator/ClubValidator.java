package br.com.meli.soccer.match_manager.service.validator;

import br.com.meli.soccer.match_manager.model.enums.AcronymStatesEnum;
import br.com.meli.soccer.match_manager.model.enums.ExceptionsEnum;
import br.com.meli.soccer.match_manager.model.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.model.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubValidator {

    private final ClubRepository clubRepository;

    public void validate(Club club) {
        throwIfInvalidClubRequestFields(club);
        throwIfClubExists(club);
    }

    private void throwIfInvalidClubRequestFields(Club club) {
        if(!AcronymStatesEnum.isValidAcronym(club.getStateAcronym())) {
            throw new InvalidFieldsException(ExceptionsEnum.NON_EXISTENT_ACRONYM_STATE.getValue());
        }
    }

    private void throwIfClubExists(Club club) {

        clubRepository.findByNameAndStateAcronym(
                club.getName(),
                club.getStateAcronym()
        ).ifPresent(
                clubEqualFounded -> {
                    boolean isSameClub = club.getId() != null && club.getId().equals(clubEqualFounded.getId());
                    if(!isSameClub) {
                        throw new CreationConflictException(ExceptionsEnum.CLUB_EXISTS.getValue());
                    }
                }
        );
    }
}
