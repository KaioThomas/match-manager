package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.model.dto.request.MatchRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.request.filter.MatchFilterRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.model.entity.Match;
import br.com.meli.soccer.match_manager.model.entity.Stadium;
import br.com.meli.soccer.match_manager.model.entity.specification.MatchSpecification;
import br.com.meli.soccer.match_manager.model.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.model.exception.NotFoundException;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.repository.MatchRepository;
import br.com.meli.soccer.match_manager.repository.StadiumRepository;
import br.com.meli.soccer.match_manager.service.MatchService;
import br.com.meli.soccer.match_manager.service.converter.MatchConverter;
import br.com.meli.soccer.match_manager.service.validator.MatchValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MathServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final ClubRepository clubRepository;
    private final StadiumRepository stadiumRepository;

    private final MatchValidator matchValidator;

    @Override
    @Transactional
    public MatchResponseDTO create(MatchRequestDTO matchRequestDTO) {
        Match match = this.createMatchEntity(matchRequestDTO);
        this.matchValidator.validateCreate(match);
        return MatchConverter.toResponseDTO(this.matchRepository.save(match));
    }

    @Override
    @Transactional
    public MatchResponseDTO update(MatchRequestDTO matchRequestDTO) {
        Match match = this.createMatchEntity(matchRequestDTO);

        this.matchValidator.validateUpdate(match);
        return MatchConverter.toResponseDTO(this.matchRepository.save(match));
    }

    @Override
    @Transactional
    public MatchResponseDTO getById(String id) {
        Match match = this.matchRepository.findById(id).orElseThrow(() -> new NotFoundException("Match not found"));
        return MatchConverter.toResponseDTO(match);
    }

    @Override
    @Transactional
    public List<MatchResponseDTO> getAll(MatchFilterRequestDTO matchFilterRequestDTO) {

        PageRequest pageRequest = PageRequest.of(
                matchFilterRequestDTO.getPageNumber(),
                matchFilterRequestDTO.getSize(),
                Sort.Direction.valueOf(matchFilterRequestDTO.getDirection()),
                matchFilterRequestDTO.getOrderBy()
        );

        Specification<Match> matchSpecification = MatchSpecification.findAllMatchsByFilledFields(matchFilterRequestDTO);

        return this.matchRepository.findAll(matchSpecification, pageRequest)
                .map(MatchConverter::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        this.matchValidator.validateMatchExists(id);
        this.matchRepository.deleteById(id);
    }

    private Match createMatchEntity(MatchRequestDTO matchRequestDTO) {
        Club homeClub = this.clubRepository.findById(matchRequestDTO.homeClubId()).orElseThrow(() -> new InvalidFieldsException("The club is invalid"));
        Club visitingClub = this.clubRepository.findById(matchRequestDTO.visitingClubId()).orElseThrow(() -> new InvalidFieldsException("The club is invalid"));
        Stadium stadium = this.stadiumRepository.findById(matchRequestDTO.stadiumId()).orElseThrow(() -> new InvalidFieldsException("The stadium is invalid"));

        return MatchConverter.toEntity(matchRequestDTO, stadium, homeClub, visitingClub);
    }
}
