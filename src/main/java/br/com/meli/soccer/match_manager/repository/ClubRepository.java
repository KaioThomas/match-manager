package br.com.meli.soccer.match_manager.repository;

import br.com.meli.soccer.match_manager.model.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    List<Club> findAllByName(String name);
}
