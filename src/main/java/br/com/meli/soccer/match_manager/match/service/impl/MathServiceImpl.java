package br.com.meli.soccer.match_manager.match.service.impl;

import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import br.com.meli.soccer.match_manager.match.dto.MatchTotalRetrospect;
import br.com.meli.soccer.match_manager.match.dto.OpponentDTO;
import br.com.meli.soccer.match_manager.match.dto.MatchHistoryDTO;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchRequestDTO;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchFilterRequestDTO;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchHistoryResponse;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.club.entity.Club;
import br.com.meli.soccer.match_manager.match.entity.Match;
import br.com.meli.soccer.match_manager.match.validation.MatchValidator;
import br.com.meli.soccer.match_manager.stadium.entity.Stadium;
import br.com.meli.soccer.match_manager.match.entity.specification.MatchSpecification;
import br.com.meli.soccer.match_manager.common.exception.NotFoundException;
import br.com.meli.soccer.match_manager.club.repository.ClubRepository;
import br.com.meli.soccer.match_manager.match.repository.MatchRepository;
import br.com.meli.soccer.match_manager.stadium.repository.StadiumRepository;
import br.com.meli.soccer.match_manager.match.service.MatchService;
import br.com.meli.soccer.match_manager.match.mapper.MatchMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants.*;

@Service
@RequiredArgsConstructor
public class MathServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final ClubRepository clubRepository;
    private final StadiumRepository stadiumRepository;

    private final MatchValidator matchValidator;

    @Override
    @Transactional
    public MatchResponseDTO create(MatchCreateRequest matchCreateRequest) {
        Match match = this.createMatchEntity(matchCreateRequest);

        this.matchValidator.validate(match);

        return MatchMapper.toResponseDTO(this.matchRepository.save(match));
    }

    @Override
    @Transactional
    public MatchResponseDTO update(MatchUpdateRequest matchUpdateRequest) {
        Match match = this.createMatchEntity(matchUpdateRequest);
        
        this.matchValidator.validate(match);

        return MatchMapper.toResponseDTO(this.matchRepository.save(match));
    }

    @Override
    @Transactional
    public MatchResponseDTO getById(String id) {

        Match match = this.matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MATCH_NOT_FOUND));

        return MatchMapper.toResponseDTO(match);
    }

    @Override
    @Transactional
    public List<MatchResponseDTO> getAll(MatchFilterRequestDTO matchFilterRequestDTO, Pageable pageable) {

        Specification<Match> matchSpecification = MatchSpecification.matchsByFilledFields(matchFilterRequestDTO);

        return this.matchRepository.findAll(matchSpecification, pageable)
                .map(MatchMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        this.matchValidator.validateMatchExists(id);
        this.matchRepository.deleteById(id);
    }

    @Override
    @Transactional
    public MatchHistoryResponse getMatchHistoryByOpponent(String clubId, ClubTypeEnum clubRequiredActing, String opponendId) {
        List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchRetrospect(clubId, clubRequiredActing, opponendId));
        List<MatchHistoryDTO> matchHistoryDTOS = this.getClubRetrospectByOpponent(matches, clubId);
        return new MatchHistoryResponse(matchHistoryDTOS);
    }

    @Override
    @Transactional
    public MatchTotalRetrospect getMatchRetrospect(String clubId, ClubTypeEnum clubRequiredActing) {
        List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchRetrospect(clubId, clubRequiredActing, null));
        return this.getTotalClubRetrospect(matches, clubId);
    }

    private MatchTotalRetrospect getTotalClubRetrospect(List<Match> matches, String clubId) {
        MatchTotalRetrospect matchTotalRetrospect = new MatchTotalRetrospect();

        for(Match match : matches) {
            boolean playedAtHome = match.getHomeClub().getId().equals(clubId);

            int goalsConceded = playedAtHome ? match.getVisitingClubGoals() : match.getHomeClubGoals();
            int goalsScored = playedAtHome ? match.getHomeClubGoals() : match.getVisitingClubGoals();

            matchTotalRetrospect.generateResult(goalsScored, goalsConceded);
        }

        return matchTotalRetrospect;
    }


    private List<MatchHistoryDTO> getClubRetrospectByOpponent(List<Match> matches, String clubId) {

        HashMap<String, MatchHistoryDTO> totalRetrospect = new HashMap<>();

        for(Match match : matches) {
            boolean playedAtHome = match.getHomeClub().getId().equals(clubId);

            String opponentId = playedAtHome ? match.getVisitingClub().getId() : match.getHomeClub().getId();
            String opponentName = playedAtHome ? match.getVisitingClub().getName() : match.getHomeClub().getName();

            int goalsConceded = playedAtHome ? match.getVisitingClubGoals() : match.getHomeClubGoals();
            int goalsScored = playedAtHome ? match.getHomeClubGoals() : match.getVisitingClubGoals();

            totalRetrospect.computeIfAbsent(opponentId, _ -> new MatchHistoryDTO(new OpponentDTO(opponentId, opponentName)))
                    .generateResult(goalsScored, goalsConceded);
        }

        return totalRetrospect.values()
                .stream()
                .toList();
    }

    private Match createMatchEntity(MatchRequestDTO matchRequestDTO) {
        Match match;

        if(matchRequestDTO.getClass().equals(MatchCreateRequest.class)) {
            match = new Match();
        } else {
            MatchUpdateRequest matchUpdateRequest = (MatchUpdateRequest) matchRequestDTO;
            match = this.matchRepository.findById(matchUpdateRequest.id())
                    .orElseThrow(() -> new NotFoundException(MATCH_NOT_FOUND));
        }

        Stadium stadium = this.stadiumRepository.findById(matchRequestDTO.stadiumId())
                .orElseThrow(() -> new NotFoundException(STADIUM_NOT_FOUND));

        Club visitingClub = this.clubRepository.findById(matchRequestDTO.visitingClubResult().id())
                .orElseThrow(() -> new NotFoundException(CLUB_NOT_FOUND));

        Club homeClub = this.clubRepository.findById(matchRequestDTO.homeClubResult().id()).
                orElseThrow(() -> new NotFoundException(CLUB_NOT_FOUND));

        return MatchMapper.toEntity(matchRequestDTO, stadium, homeClub, visitingClub, match);

    }
}
