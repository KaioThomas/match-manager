package br.com.meli.soccer.match_manager.match.repository;

import br.com.meli.soccer.match_manager.match.dto.Ranking;
import br.com.meli.soccer.match_manager.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, String>, JpaSpecificationExecutor<Match> {

    List<Match> findAllByHomeClubId(String id);
    List<Match> findAllByVisitingClubId(String id);

    @Query(value = """
       SELECT 
           c.id,
           c.name,
           SUM(
               CASE
                       WHEN (m.home_club_id = c.id) AND m.home_club_goals > m.visiting_club_goals THEN 3
                       WHEN (m.visiting_club_id = c.id) AND m.visiting_club_goals > m.home_club_goals THEN 3
                       WHEN m.home_club_goals = m.visiting_club_goals THEN 1
                       ELSE 0
                       END
               ) AS total_score,
           SUM(
               CASE
                       WHEN (m.home_club_id = c.id) AND m.home_club_goals > m.visiting_club_goals THEN 1
                       WHEN (m.visiting_club_id = c.id) AND m.visiting_club_goals > m.home_club_goals THEN 1
                       ELSE 0
                       END
               ) AS total_victories,
           SUM(
               CASE
                       WHEN (m.home_club_id = c.id) THEN home_club_goals
                       WHEN (m.visiting_club_id = c.id) THEN visiting_club_goals
                       ELSE 0
                       END
               ) AS total_goals,
           COUNT(m.id) AS total_games
       FROM club c
               LEFT JOIN football_match m ON c.id = m.home_club_id OR c.id = m.visiting_club_id
           GROUP BY c.id, c.name
               HAVING SUM(
                   CASE
                           WHEN (m.home_club_id = c.id AND LEAST(m.home_club_goals, m.home_club_goals - m.visiting_club_goals) > 0) THEN 1
                           WHEN (m.visiting_club_goals = c.id AND LEAST(m.visiting_club_goals, m.visiting_club_goals - m.home_club_goals) > 0) THEN 1
                           ELSE 0
                           END
                          ) > 0
       ORDER BY
         total_score DESC, 
         total_goals DESC, 
         total_victories DESC, 
         total_games DESC
    """
, nativeQuery = true)
    List<Ranking> findRanking();
}
