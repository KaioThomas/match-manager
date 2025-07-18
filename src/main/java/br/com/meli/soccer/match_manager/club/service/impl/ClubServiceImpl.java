package br.com.meli.soccer.match_manager.club.service.impl;

import br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants.CLUB;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.request.ClubUpdateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.common.exception.NotFoundException;
import br.com.meli.soccer.match_manager.club.entity.Club;
import br.com.meli.soccer.match_manager.club.repository.ClubRepository;
import br.com.meli.soccer.match_manager.club.mapper.ClubMapper;
import br.com.meli.soccer.match_manager.club.service.ClubService;
import br.com.meli.soccer.match_manager.club.validation.ClubValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final ClubValidator clubValidator;

    @Override
    @Transactional
    public ClubResponse create(ClubCreateRequest clubCreateRequest) {

        Club club = ClubMapper.toEntity(clubCreateRequest, new Club());

        this.clubValidator.validateCreation(club);

        return ClubMapper.toResponse(this.clubRepository.save(club));
    }

    @Override
    @Transactional
    public ClubResponse update(ClubUpdateRequest clubUpdateRequest) {
        Club oldClub = this.clubRepository.findById(clubUpdateRequest.getId()).orElseThrow(() -> new NotFoundException(CLUB.NOT_FOUND));
        Club updatedClub = ClubMapper.toEntity(clubUpdateRequest, oldClub);

        this.clubValidator.validateUpdate(updatedClub);

        return ClubMapper.toResponse(this.clubRepository.save(updatedClub));
    }

    @Override
    @Transactional
    public ClubResponse getById(String id) {
        Club club = this.clubRepository.findById(id).orElseThrow(() -> new NotFoundException(CLUB.NOT_FOUND));
        return ClubMapper.toResponse(club);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Club club = this.clubRepository.findById(id).orElseThrow(() -> new NotFoundException(CLUB.NOT_FOUND));
        club.setActive(false);
        this.clubRepository.save(club);
    }

    @Override
    @Transactional
    public List<ClubResponse> getAll(
            String name,
            Boolean active,
            String acronymState,
            Pageable pageable
    ) {
        Club clubExample = new Club();
        clubExample.setStateAcronym(acronymState);
        clubExample.setName(name);
        clubExample.setActive(active);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Club> objectExample = Example.of(clubExample, exampleMatcher);

        Page<Club> page = this.clubRepository.findAll(objectExample, pageable);

        return page.getContent()
                .stream()
                .map(ClubMapper::toResponse)
                .toList();
    }
}
