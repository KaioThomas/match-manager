package br.com.meli.soccer.match_manager.repository;

import br.com.meli.soccer.match_manager.model.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface MatchRepository extends JpaRepository<Match, String> {

    List<Match> findAllByHomeClubId(String id);
    List<Match> findAllByVisitingClubId(String id);

    @Query(value = """
         SELECT 1 FROM football_match 
         WHERE home_club_id = :id 
         OR visiting_club_id = :id 
         AND date_time < :creationDate;
    """, nativeQuery = true)
    int existsClubMatchBeforeCreationDate(String id, LocalDate creationDate);
}
