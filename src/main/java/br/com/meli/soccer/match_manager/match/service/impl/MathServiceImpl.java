package br.com.meli.soccer.match_manager.match.service.impl;

import br.com.meli.soccer.match_manager.common.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.match.dto.ClubData;
import br.com.meli.soccer.match_manager.match.dto.ClubSummary;
import br.com.meli.soccer.match_manager.match.dto.GeneralRetrospect;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchActingFilter;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchThrashingFilter;
import br.com.meli.soccer.match_manager.match.dto.response.*;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
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

import java.util.*;

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
    public MatchResponse create(MatchCreateRequest matchCreateRequest) {
        Match match = this.createMatchEntity(matchCreateRequest);

        this.matchValidator.validate(match);

        return MatchMapper.toResponseDTO(this.matchRepository.save(match));
    }

    @Override
    @Transactional
    public MatchResponse update(MatchUpdateRequest matchUpdateRequest) {
        Match match = this.createMatchEntity(matchUpdateRequest);
        
        this.matchValidator.validate(match);

        return MatchMapper.toResponseDTO(this.matchRepository.save(match));
    }

    @Override
    @Transactional
    public MatchResponse getById(String id) {

        Match match = this.matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MATCH.NOT_FOUND));

        return MatchMapper.toResponseDTO(match);
    }

    @Override
    @Transactional
    public List<MatchResponse> getAll(String clubId, MatchThrashingFilter matchThrashingFilter, Pageable pageable) {

        Specification<Match> matchSpecification = MatchSpecification.matchsByFilledFields(clubId, matchThrashingFilter.thrashing());

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
    public List<RetrospectByOpponent> getRetrospectByOpponents(String clubId, MatchActingFilter matchActingFilter) {
        if(!this.clubRepository.existsById(clubId)) {
            throw new NotFoundException(CLUB.NOT_FOUND);
        }
        List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchRetrospect(clubId, matchActingFilter.clubRequiredActing()));
        return this.createRetrospectByOpponentResponse(matches, clubId);
    }

    @Override
    @Transactional
    public GeneralRetrospect getGeneralRetrospect(String clubId, MatchActingFilter matchActingFilter) {
        if(!this.clubRepository.existsById(clubId)) {
            throw new NotFoundException(CLUB.NOT_FOUND);
        }
        List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchRetrospect(clubId, matchActingFilter.clubRequiredActing()));
        return this.createGeneralRetrospectResponse(matches, clubId);
    }

    @Override
    @Transactional
    public DirectConfrontationsResponse getDirectConfrontations(String idClubA, String idClubB) {
        Club clubA = this.clubRepository.findById(idClubA).orElseThrow(() -> new NotFoundException(CLUB.NOT_FOUND));
        Club clubB = this.clubRepository.findById(idClubB).orElseThrow(() -> new NotFoundException(CLUB.NOT_FOUND));

        ClubSummary clubASummary = new ClubSummary();
        clubASummary.setId(clubA.getId());
        clubASummary.setName(clubA.getName());

        ClubSummary clubBSummary = new ClubSummary();
        clubBSummary.setId(clubB.getId());
        clubBSummary.setName(clubB.getName());

        List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchDirectConfrontationsRetrospect(idClubA, idClubB));
        return createDirectConfrontationsRetrospectResponse(matches, clubASummary, clubBSummary);
    }

    @Override
    public List<RankingResponse> getRanking() {
        List<RankingResponse> rankingResponses = defineRanking();
        rankingResponses.sort(Comparator.comparing(RankingResponse::getTotalScore));
        return rankingResponses;

    }

    private List<RankingResponse> defineRanking() {
        List<Club> clubs = this.clubRepository.findAll();

        if(clubs.isEmpty()) {
            return List.of();
        }

        Map<String,RankingResponse> rankingResponseMap = new HashMap<>();

        for(Club club : clubs) {
            List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchRetrospect(club.getId(), null));

            if(matches.isEmpty()) {
                continue;
            }

            GeneralRetrospect totalClubRetrospect = createGeneralRetrospectResponse(matches, club.getId());
            String clubId = club.getId();
            ClubData clubData = new ClubData(clubId, club.getName());

            rankingResponseMap.computeIfAbsent(clubId, _ -> new RankingResponse(clubData))
                    .generateRanking(
                            totalClubRetrospect.getTotalVictories(),
                            totalClubRetrospect.getTotalDraws(),
                            totalClubRetrospect.getGoalsScored(),
                            matches.size()
                    );
        }

        return new ArrayList<>(rankingResponseMap.values());
    }

    private GeneralRetrospect createGeneralRetrospectResponse(List<Match> matches, String clubId) {
        GeneralRetrospect generalRetrospect = new GeneralRetrospect();

        for(Match match : matches) {
            boolean playedAtHome = match.getHomeClub().getId().equals(clubId);

            int goalsConceded = playedAtHome ? match.getVisitingClubGoals() : match.getHomeClubGoals();
            int goalsScored = playedAtHome ? match.getHomeClubGoals() : match.getVisitingClubGoals();

            generalRetrospect.generateResult(goalsScored, goalsConceded);
        }

        return generalRetrospect;
    }

    private DirectConfrontationsResponse createDirectConfrontationsRetrospectResponse(List<Match> matches, ClubSummary clubA, ClubSummary clubB) {
        for(Match match : matches) {
            boolean isAHomeClub = match.getHomeClub().getId().equals(clubA.getId());

            int goalsClubScoredA = isAHomeClub ? match.getHomeClubGoals() : match.getVisitingClubGoals();
            int goalsClubScoredB = isAHomeClub ? match.getVisitingClubGoals() : match.getHomeClubGoals();

            clubA.generateResult(goalsClubScoredA, goalsClubScoredB);
            clubB.generateResult(goalsClubScoredB, goalsClubScoredA);
        }

        return new DirectConfrontationsResponse(clubA, clubB, matches.stream().map(MatchMapper::toResponseDTO).toList());
    }


    private List<RetrospectByOpponent> createRetrospectByOpponentResponse(List<Match> matches, String clubId) {

        HashMap<String, RetrospectByOpponent> totalRetrospect = new HashMap<>();

        for(Match match : matches) {
            boolean playedAtHome = match.getHomeClub().getId().equals(clubId);

            String opponentId = playedAtHome ? match.getVisitingClub().getId() : match.getHomeClub().getId();
            String opponentName = playedAtHome ? match.getVisitingClub().getName() : match.getHomeClub().getName();

            int goalsConceded = playedAtHome ? match.getVisitingClubGoals() : match.getHomeClubGoals();
            int goalsScored = playedAtHome ? match.getHomeClubGoals() : match.getVisitingClubGoals();

            totalRetrospect.computeIfAbsent(opponentId, _ -> new RetrospectByOpponent(new ClubData(opponentId, opponentName)))
                    .generateResult(goalsScored, goalsConceded);
        }

        return totalRetrospect.values()
                .stream()
                .toList();
    }

    private Match createMatchEntity(MatchRequest matchRequest) {
        Match match;

        if(matchRequest.getClass().equals(MatchCreateRequest.class)) {
            match = new Match();
        } else {
            MatchUpdateRequest matchUpdateRequest = (MatchUpdateRequest) matchRequest;
            match = this.matchRepository.findById(matchUpdateRequest.getId())
                    .orElseThrow(() -> new NotFoundException(MATCH.NOT_FOUND));
        }

        Stadium stadium = this.stadiumRepository.findById(matchRequest.getStadiumId())
                .orElseThrow(() -> new InvalidFieldsException(STADIUM.NOT_FOUND));

        Club visitingClub = this.clubRepository.findById(matchRequest.getVisitingClubResult().id())
                .orElseThrow(() -> new InvalidFieldsException(CLUB.NOT_FOUND));

        Club homeClub = this.clubRepository.findById(matchRequest.getHomeClubResult().id()).
                orElseThrow(() -> new InvalidFieldsException(CLUB.NOT_FOUND));

        return MatchMapper.toEntity(matchRequest, stadium, homeClub, visitingClub, match);

    }
}
