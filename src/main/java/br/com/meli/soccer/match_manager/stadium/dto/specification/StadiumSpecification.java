package br.com.meli.soccer.match_manager.stadium.dto.specification;

import br.com.meli.soccer.match_manager.stadium.entity.Stadium;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StadiumSpecification {

    public static Specification<Stadium> otherStadiumSameName(String name, String id) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(cb.upper(root.get("name")), name));

            if(id != null) predicates.add(cb.notEqual(root.get("id"), id));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
