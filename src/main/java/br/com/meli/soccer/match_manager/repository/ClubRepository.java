package com.meli.soccer.match_manager.repository;

import com.meli.soccer.match_manager.model.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
}
