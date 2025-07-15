package br.com.meli.soccer.match_manager.match.entity.specification.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MatchPredicateBuilder<T> {

    @Getter
    private final Root<T> root;
    @Getter
    private final CriteriaBuilder criteriaBuilder;
    @Getter
    private final List<Predicate> predicates = new ArrayList<>();

    private static final String HOME_CLUB = "homeClub";
    private static final String VISITING_CLUB = "visitingClub";
    private static final String STADIUM = "stadium";
    private static final String DATE_TIME = "dateTime";
    private static final String ID = "id";
    private static final String DATE = "DATE";

    public Predicate build() {
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public MatchPredicateBuilder<T> dateTimeToLocalDateLessThan(LocalDate date) {
        if(date != null) {
            Expression<LocalDate> formattedDate = this.getDateFromDateTime();
            predicates.add(criteriaBuilder.lessThan(formattedDate, date));
        }

        return this;
    }

    public MatchPredicateBuilder<T> dateTimeToLocalDateEqual(LocalDate date) {
        if(date != null) {
            Expression<LocalDate> formattedDate = this.getDateFromDateTime();
            predicates.add(criteriaBuilder.equal(formattedDate, date));
        }

        return this;
    }

    private Expression<LocalDate> getDateFromDateTime() {
        return criteriaBuilder.function(DATE, LocalDate.class, root.get(DATE_TIME));
    }

    public MatchPredicateBuilder<T> findClub(String id) {
        if(id != null) {
            predicates.add(criteriaBuilder.or(equalsHomeClubIdPredicate(id), equalsVisitingClubIdPredicate(id)));
        }
        return this;
    }

    public MatchPredicateBuilder<T> equalsVisitingClubId(String id) {
        if(id != null) {
            predicates.add(equalsVisitingClubIdPredicate(id));
        }
        return this;
    }

    public MatchPredicateBuilder<T> equalsStadiumId(String id) {
        if(id != null) {
            predicates.add(criteriaBuilder.equal(root.get(STADIUM).get(ID), id));
        }
        return this;
    }

    public MatchPredicateBuilder<T> equalsHomeClubId(String id) {
        if(id != null) {
            predicates.add(equalsHomeClubIdPredicate(id));
        }
        return this;
    }

    private Predicate equalsVisitingClubIdPredicate(String id) {
        return criteriaBuilder.equal(root.get(VISITING_CLUB).get(ID), id);
    }

    private Predicate equalsHomeClubIdPredicate(String id) {
        return criteriaBuilder.equal(root.get(HOME_CLUB).get(ID), id);
    }

}
