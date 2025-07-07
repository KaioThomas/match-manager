package br.com.meli.soccer.match_manager.service.validator;

import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.model.entity.Match;
import br.com.meli.soccer.match_manager.model.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.model.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchValidator {

    private final MatchRepository matchRepository;
    private final ClubRepository clubRepository;

    public void validate(Match match) {
        throwIfClubsAreEquals(match);
        throwIfMatchResultIsLowerThanZero(match);
        throwIfClubsAreInvalid(match);
        throwIfClubAlreadyHasMatch(match);
    }

    private void throwIfClubsAreEquals(Match match) {
        if(match.getHomeClub().getId().equals(match.getVisitingClub().getId())) {
            throw new InvalidFieldsException("The clubs at the match cannot be the same");
        }
    }

    private void throwIfMatchResultIsLowerThanZero(Match match) {
        if(match.getHomeClubGoals() < 0 || match.getVisitingClubGoals() < 0) {
            throw new InvalidFieldsException("The number of goals cannot be lower than zero");
        }
    }

    private void throwIfClubsAreInvalid(Match match) {

        Club visitingClub = this.clubRepository.findById(match.getVisitingClub().getId()).orElse(null);
        Club homeClub = this.clubRepository.findById(match.getHomeClub().getId()).orElse(null);

        if(visitingClub == null || homeClub == null) {
            throw new InvalidFieldsException("The club is invalid");
        }

        LocalDate matchDate = match.getDateTime().toLocalDate();

        if(matchDate.isBefore(visitingClub.getCreationDate()) || matchDate.isBefore(homeClub.getCreationDate())) {
            throw new CreationConflictException("The match date cannot be before the clubs creation date");
        }
    }

    private void throwIfClubAlreadyHasMatch(Match match) {
        List<Match> homeClubMatches = this.matchRepository.findAllByHomeClubId(match.getHomeClub().getId());
        boolean isInvalidHomeClubMatchDate = homeClubMatches.stream().anyMatch(matchHomeclub -> areMatchesLessThan48HoursFromEachOther(match, matchHomeclub));

        if(isInvalidHomeClubMatchDate) {
            throw new CreationConflictException("The interval from last match must be greater than 48 hours");
        }

        List<Match> visitingClubMatches = this.matchRepository.findAllByVisitingClubId(match.getVisitingClub().getId());
        boolean isInvalidVisitingClubMatchDate = visitingClubMatches.stream().anyMatch(matchVisitingclub -> areMatchesLessThan48HoursFromEachOther(match, matchVisitingclub));

        if(isInvalidVisitingClubMatchDate) {
            throw new CreationConflictException("The interval from last match must be greater than 48 hours");
        }
    }

    private boolean areMatchesLessThan48HoursFromEachOther(Match match, Match matchClub) {
        long hoursOfDiference = Math.abs(Duration.between(match.getDateTime(), matchClub.getDateTime()).toHours());
        return hoursOfDiference < 48;
    }
}
