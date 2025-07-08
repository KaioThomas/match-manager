package br.com.meli.soccer.match_manager.repository;

import br.com.meli.soccer.match_manager.model.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, String>, JpaSpecificationExecutor<Stadium> {
}
