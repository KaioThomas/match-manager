package br.com.meli.soccer.match_manager.club.validation;

import br.com.meli.soccer.match_manager.club.entity.specification.ClubSpecification;
import br.com.meli.soccer.match_manager.common.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.club.entity.Club;
import br.com.meli.soccer.match_manager.club.repository.ClubRepository;
import br.com.meli.soccer.match_manager.match.entity.specification.MatchSpecification;
import br.com.meli.soccer.match_manager.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants.CLUB_DUPLICATED;
import static br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants.CREATION_DATE_AFTER_MATCH;

@Service
@RequiredArgsConstructor
public class ClubValidator {

    private final ClubRepository clubRepository;
    private final MatchRepository matchRepository;

    public void validateCreation(Club club) {
        validateClubNotDuplicated(club);
    }

    public void validateUpdate(Club club) {
        validateClubNotDuplicated(club);
        validateClubHasMatchBeforeCreationDate(club);
    }

    public void validateClubNotDuplicated(Club club) {

        boolean hasDuplicatedClub = this.clubRepository.exists(ClubSpecification.duplicatedClub(club));

        if(hasDuplicatedClub) {
            throw new CreationConflictException(CLUB_DUPLICATED);
        }
    }

    public void validateClubHasMatchBeforeCreationDate(Club club) {
        boolean existsMatchBeforeCreationDate = this.matchRepository.exists(MatchSpecification.creationDateAfterClubMatch(club));

        if(existsMatchBeforeCreationDate) throw new CreationConflictException(CREATION_DATE_AFTER_MATCH);

    }
}
