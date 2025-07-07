package br.com.meli.soccer.match_manager.repository;

import br.com.meli.soccer.match_manager.model.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, String> {

    Optional<Club> findByNameAndStateAcronym(String name, String acronymState);
}
