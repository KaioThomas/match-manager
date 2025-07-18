package br.com.meli.soccer.match_manager.match.entity.specification;

import br.com.meli.soccer.match_manager.club.entity.Club;
import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import br.com.meli.soccer.match_manager.match.entity.Match;
import br.com.meli.soccer.match_manager.match.entity.specification.predicate.MatchPredicateBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchSpecification {

    public static Specification<Match> matchsByFilledFields(String clubId, Boolean thrashing) {
        return (root, cq, cb) -> {
            MatchPredicateBuilder<Match> matchPredicateBuilder = new MatchPredicateBuilder<>(root, cb);
            matchPredicateBuilder.findClub(clubId);

            if(Boolean.TRUE.equals(thrashing)) {
                matchPredicateBuilder.findByThrashing();
            }

            return matchPredicateBuilder.build();
        };

    }

    public static Specification<Match> creationDateAfterClubMatch(Club club) {
        return (root, cq, cb) ->
                new MatchPredicateBuilder<>(root, cb)
                    .findClub(club.getId())
                    .dateTimeToLocalDateLessThan(club.getCreationDate()).build();
    }

    public static Specification<Match> matchAtStadiumAtDate(Match match) {
        return (root, cq, cb) ->
                new MatchPredicateBuilder<>(root, cb)
                    .equalsStadiumId(match.getStadium().getId())
                    .dateTimeToLocalDateEqual(match.getDateTime().toLocalDate())
                    .build();
    }

    public static Specification<Match> matchRetrospect(String clubId, ClubTypeEnum clubActing) {
        return (root, cq, cb) -> {

            MatchPredicateBuilder<Match> matchPredicateBuilder = new MatchPredicateBuilder<>(root, cb);

            if(clubActing == null) {
                matchPredicateBuilder.findClub(clubId);
            } else if(clubActing.equals(ClubTypeEnum.HOME)) {
                matchPredicateBuilder.equalsHomeClubId(clubId);
            } else if(clubActing.equals(ClubTypeEnum.VISITING)) {
                matchPredicateBuilder.equalsVisitingClubId(clubId);
            }

            return matchPredicateBuilder.build();
        };
    }

    public static Specification<Match> matchDirectConfrontationsRetrospect(String idClubA, String idClubB) {
        return (root, cq, cb) ->
                new MatchPredicateBuilder<>(root, cb).equalsClubAndClub(idClubA, idClubB).build();
    }

}
