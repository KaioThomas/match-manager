package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.model.dto.request.ClubRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.model.exception.NotFoundException;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.service.converter.ClubConverter;
import br.com.meli.soccer.match_manager.service.ClubService;
import br.com.meli.soccer.match_manager.service.validator.ClubValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final ClubValidator clubValidator;

    @Override
    @Transactional
    public ClubResponseDTO create(ClubRequestDTO clubRequestDTO) {

        Club club = ClubConverter.toEntity(clubRequestDTO);

        this.clubValidator.validate(club);

        return ClubConverter.toResponseDTO(this.clubRepository.save(club));
    }

    @Override
    @Transactional
    public ClubResponseDTO update(ClubRequestDTO clubRequestDTO, String id) {

        Club club = ClubConverter.toEntity(clubRequestDTO);

        this.clubValidator.validate(club);

        boolean clubExists = this.clubRepository.existsById(UUID.fromString(id));

        if(!clubExists) {
            throw new NotFoundException("You can't update a club that doesn't exist");
        }

        return ClubConverter.toResponseDTO(this.clubRepository.save(club));
    }

    @Override
    @Transactional
    public ClubResponseDTO getById(String id) {
        Club club = this.clubRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("Club not found"));
        return ClubConverter.toResponseDTO(club);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Club club = this.clubRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("Club not found"));
        club.setActive(false);
        this.clubRepository.save(club);
    }

    @Override
    @Transactional
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
        clubExample.setStateAcronym(acronymState);
        clubExample.setName(name);
        clubExample.setActive(active);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Club> objectExample = Example.of(clubExample, exampleMatcher);

        PageRequest pageRequest = PageRequest.of(pageNumber, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Club> page = this.clubRepository.findAll(objectExample, pageRequest);
        return page.getContent()
                .stream()
                .map(ClubConverter::toResponseDTO)
                .toList();
    }
}
