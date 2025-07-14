package br.com.meli.soccer.match_manager.match.entity.specification;

import br.com.meli.soccer.match_manager.club.entity.Club;
import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchFilterRequestDTO;
import br.com.meli.soccer.match_manager.match.entity.Match;
import jakarta.persistence.criteria.Expression;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchSpecification {

    public static Specification<Match> matchsByFilledFields(MatchFilterRequestDTO matchFilterRequestDTO) {
        return (root, criterieQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (matchFilterRequestDTO.visitingClubId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("visitingClub").get("id"), matchFilterRequestDTO.visitingClubId()));
            }

            if (matchFilterRequestDTO.homeClubId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("homeClub").get("id"), matchFilterRequestDTO.homeClubId()));
            }

            if (matchFilterRequestDTO.stadiumId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("stadium").get("id"), matchFilterRequestDTO.stadiumId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Match> creationDateAfterClubMatch(Club club) {
        return (root, cq, cb) -> {
            Predicate findClub = cb.or(
                    cb.equal(root.get("homeClub").get("id"), club.getId()),
                    cb.equal(root.get("visitingClub").get("id"), club.getId())
            );
            Expression<LocalDate> formattedDate = cb.function("DATE", LocalDate.class, root.get("dateTime"));
            Predicate dateLessThenCreationDate = cb.lessThan(formattedDate, club.getCreationDate());
            return cb.and(findClub, dateLessThenCreationDate);
        };
    }

    public static Specification<Match> matchAtStadiumAtDate(Match match) {
        return (root, cq, cb) -> {
            Predicate equalsStadiumId = cb.equal(root.get("stadium").get("id"), match.getStadium().getId());
            Expression<LocalDate> formattedDate = cb.function("DATE", LocalDate.class, root.get("dateTime"));
            Predicate equalsDate = cb.equal(formattedDate, match.getDateTime().toLocalDate());
            return cb.and(equalsStadiumId, equalsDate);
        };
    }

    public static Specification<Match> matchHistory(String clubId, ClubTypeEnum clubActing) {
        return (root, cq, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            Predicate equalsHomeClubId = cb.equal(root.get("homeClub").get("id"), clubId);
            Predicate equalsVisitingClubId = cb.equal(root.get("visitingClub").get("id"), clubId);

            if(clubActing == null) {
                predicates.add(cb.or(equalsHomeClubId, equalsVisitingClubId));
            } else if(clubActing.equals(ClubTypeEnum.HOME)) {
                predicates.add(equalsHomeClubId);
            } else if(clubActing.equals(ClubTypeEnum.VISITING)) {
                predicates.add(equalsVisitingClubId);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
