package com.meli.soccer.match_manager.controller;

import com.meli.soccer.match_manager.dto.ClubDTO;
import com.meli.soccer.match_manager.model.entity.Club;
import com.meli.soccer.match_manager.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/club")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    public ResponseEntity<Club> createClub(@RequestBody ClubDTO clubDTO) {
        Club response = this.clubService.createClub(clubDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
