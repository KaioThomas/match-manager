package br.com.meli.soccer.match_manager.model.entity.specification;

import br.com.meli.soccer.match_manager.model.dto.request.filter.MatchFilterRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Match;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.criteria.Predicate;

public class MatchSpecification {

    public static Specification<Match> findAllMatchsByFilledFields(MatchFilterRequestDTO matchFilterRequestDTO) {
        return (root, criterieQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!Objects.equals(matchFilterRequestDTO.getVisitingClubId(), "0")) {
                predicates.add(criteriaBuilder.equal(root.get("visitingClub").get("id"), matchFilterRequestDTO.getVisitingClubId()));
            }

            if (!Objects.equals(matchFilterRequestDTO.getHomeClubId(), "0")) {
                predicates.add(criteriaBuilder.equal(root.get("homeClub").get("id"), matchFilterRequestDTO.getHomeClubId()));
            }

            if (!Objects.equals(matchFilterRequestDTO.getStadiumId(), "0")) {
                predicates.add(criteriaBuilder.equal(root.get("stadium").get("id"), matchFilterRequestDTO.getStadiumId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
