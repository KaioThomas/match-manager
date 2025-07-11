package br.com.meli.soccer.match_manager.club.repository;

import br.com.meli.soccer.match_manager.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, String>, JpaSpecificationExecutor<Club> {

}
