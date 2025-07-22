package br.com.meli.soccer.match_manager.club.entity.specification;

import br.com.meli.soccer.match_manager.club.entity.Club;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClubSpecification {

    public static Specification<Club> duplicatedClub(Club club) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(cb.upper(root.get("name")), club.getName().toUpperCase()));
            predicates.add(cb.equal(cb.upper(root.get("stateAcronym")), club.getStateAcronym().toUpperCase()));

            if(club.getId() != null) {
                predicates.add(cb.notEqual(root.get("id"), club.getId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
