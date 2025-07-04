package br.com.meli.soccer.match_manager.controller;

import br.com.meli.soccer.match_manager.model.dto.request.StadiumRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Stadium;
import br.com.meli.soccer.match_manager.repository.StadiumRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stadium")
@RequiredArgsConstructor
public class StadiumController {

    private final StadiumRepository stadiumRepository;

    @PostMapping
    public Stadium create(@RequestBody @Valid StadiumRequestDTO stadiumRequestDTO) {
        Stadium stadium = new Stadium();
        stadium.setName(stadiumRequestDTO.name());
        return this.stadiumRepository.save(stadium);
    }

}
