package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.model.dto.response.club.ClubResponseDTO;
import br.com.meli.soccer.match_manager.model.exception.NotFoundException;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubCreateRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.club.ClubUpdateRequestDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.service.ClubConverter;
import br.com.meli.soccer.match_manager.service.ClubService;
import br.com.meli.soccer.match_manager.service.ClubValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final ClubValidator clubValidator;

    @Override
    public ClubResponseDTO create(ClubCreateRequestDTO clubCreateRequestDTO) {

        Club club = ClubConverter.toEntity(clubCreateRequestDTO);

        this.clubValidator.validate(club);

        return ClubConverter.toResponseDTO(this.clubRepository.save(club));
    }

    @Override
    public ClubResponseDTO update(ClubUpdateRequestDTO clubUpdateRequestDTO) {

        Club club = ClubConverter.toEntity(clubUpdateRequestDTO);

        this.clubValidator.validate(club);

        boolean clubExists = this.clubRepository.existsById(club.getId());

        if(!clubExists) {
            throw new NotFoundException("You can't update a club that doesn't exist");
        }

        return ClubConverter.toResponseDTO(this.clubRepository.save(club));
    }

    @Override
    public ClubResponseDTO getById(UUID id) {
        Club club = this.clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club not found"));
        return ClubConverter.toResponseDTO(club);
    }

    @Override
    public void deleteById(UUID id) {
        Club club = this.clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club not found"));
        club.setActive(false);
        this.clubRepository.save(club);
    }

    @Override
    public List<ClubResponseDTO> getAll(
            String name,
            String acronymState,
            Boolean active,
            Integer pageNumber,
            Integer size,
            String orderBy,
            String direction
    ) {
        Club clubExample = new Club();
        clubExample.setAcronymState(acronymState);
        clubExample.setName(name);
        clubExample.setActive(active);

        ExampleMatcher exampleMatcher =  ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Club> objectExample = Example.of(clubExample, exampleMatcher);

        PageRequest pageRequest = PageRequest.of(pageNumber, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Club> page = this.clubRepository.findAll(objectExample, pageRequest);
        return page.getContent()
                .stream()
                .map(ClubConverter::toResponseDTO)
                .collect(Collectors.toList());
    }
}
