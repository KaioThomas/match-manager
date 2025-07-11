package br.com.meli.soccer.match_manager.match.repository;

import br.com.meli.soccer.match_manager.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, String>, JpaSpecificationExecutor<Match> {

    List<Match> findAllByHomeClubId(String id);
    List<Match> findAllByVisitingClubId(String id);
}
