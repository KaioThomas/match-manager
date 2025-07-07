package br.com.meli.soccer.match_manager.repository;

import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.model.entity.Match;
import br.com.meli.soccer.match_manager.model.entity.Stadium;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, String> {

    List<Match> findAllByHomeClubId(String id);
    List<Match> findAllByVisitingClubId(String id);
    List<Match> findAllByVisitingClubAndHomeClubAndStadium(Club visitingClub, Club homeClub, Stadium stadium, PageRequest pageRequest);

    @Query(value = """
         SELECT 1 FROM football_match 
         WHERE home_club_id = :id 
         OR visiting_club_id = :id 
         AND date_time < :creationDate;
    """, nativeQuery = true)
    Optional<Integer> existsClubMatchBeforeCreationDate(String id, LocalDate creationDate);

    @Query(value = """
            SELECT 1 FROM football_match
            WHERE stadium_id = :stadiumId
            AND DATE(date_time) = :date
""", nativeQuery = true)
    Optional<Integer> existsMatchAtStadiumAtDate(String stadiumId, LocalDate date);
}
