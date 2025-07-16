package br.com.meli.soccer.match_manager.match.validation;

import br.com.meli.soccer.match_manager.match.entity.specification.MatchSpecification;
import br.com.meli.soccer.match_manager.match.repository.MatchRepository;
import br.com.meli.soccer.match_manager.match.entity.Match;
import br.com.meli.soccer.match_manager.common.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.common.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants.*;

@Service
@RequiredArgsConstructor
public class MatchValidator {

    private final MatchRepository matchRepository;

    public void validate(Match match) {
        validateClubsAreEquals(match);
        validateClubsAreInvalid(match);
        validateClubAlreadyHasMatch(match);
        validateStadiumHasMatchAtSameDate(match);
    }

    public void validateMatchExists(String id) {
        boolean matchExists = this.matchRepository.existsById(id);

        if(!matchExists) throw new NotFoundException(MATCH_NOT_FOUND);
    }

    public void validateClubsAreEquals(Match match) {
        if(match.getHomeClub().getId().equals(match.getVisitingClub().getId())) {
            throw new InvalidFieldsException(EQUAL_CLUBS_AT_MATCH);
        }
    }

    public void validateClubsAreInvalid(Match match) {

        if(match.getVisitingClub() == null || match.getHomeClub() == null) {
            throw new CreationConflictException(INVALID_CLUB);
        }

        if(match.getHomeClub().getActive().equals(false) || match.getVisitingClub().getActive().equals(false)) {
            throw new CreationConflictException(INACTIVE_CLUB);
        }

        LocalDate matchDate = match.getDateTime().toLocalDate();

        if(matchDate.isBefore(match.getVisitingClub().getCreationDate()) || matchDate.isBefore(match.getHomeClub().getCreationDate())) {
            throw new CreationConflictException(MATCH_BEFORE_CREATION_DATE);
        }
    }

    public void validateClubAlreadyHasMatch(Match match) {
        List<Match> homeClubMatches = this.matchRepository.findAllByHomeClubId(match.getHomeClub().getId());
        boolean isInvalidHomeClubMatchDate = homeClubMatches.stream().anyMatch(matchHomeClub -> areMatchesLessThan48HoursFromEachOther(match, matchHomeClub));

        if(isInvalidHomeClubMatchDate) {
            throw new CreationConflictException(INTERVAL_LESS_THAN_48_HOURS);
        }

        List<Match> visitingClubMatches = this.matchRepository.findAllByVisitingClubId(match.getVisitingClub().getId());
        boolean isInvalidVisitingClubMatchDate = visitingClubMatches.stream().anyMatch(matchVisitingClub -> areMatchesLessThan48HoursFromEachOther(match, matchVisitingClub));

        if(isInvalidVisitingClubMatchDate) {
            throw new CreationConflictException(INTERVAL_LESS_THAN_48_HOURS);
        }
    }

    public void validateStadiumHasMatchAtSameDate(Match match) {
        boolean existsMatchAtStadiumAtDate = this.matchRepository.exists(MatchSpecification.matchAtStadiumAtDate(match));

        if(existsMatchAtStadiumAtDate) {
            throw new CreationConflictException(STADIUM_ALREADY_HAS_MATCH);
        }
    }

    private boolean areMatchesLessThan48HoursFromEachOther(Match match, Match matchClub) {
        long hoursOfDiference = Math.abs(Duration.between(match.getDateTime(), matchClub.getDateTime()).toHours());
        return hoursOfDiference < 48;
    }
}
