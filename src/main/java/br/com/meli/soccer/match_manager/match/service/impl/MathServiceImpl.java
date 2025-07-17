package br.com.meli.soccer.match_manager.match.service.impl;

import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import br.com.meli.soccer.match_manager.common.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.match.dto.ClubData;
import br.com.meli.soccer.match_manager.match.dto.response.ClubTotalRetrospectResponse;
import br.com.meli.soccer.match_manager.match.dto.Opponent;
import br.com.meli.soccer.match_manager.match.dto.response.RankingResponse;
import br.com.meli.soccer.match_manager.match.dto.response.RetrospectByOpponentResponse;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchRequest;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchFilterRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponse;
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
                .orElseThrow(() -> new NotFoundException(MATCH_NOT_FOUND));

        return MatchMapper.toResponseDTO(match);
    }

    @Override
    @Transactional
    public List<MatchResponse> getAll(MatchFilterRequest matchFilterRequest, Pageable pageable) {

        Specification<Match> matchSpecification = MatchSpecification.matchsByFilledFields(matchFilterRequest);

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
    public List<RetrospectByOpponentResponse> getTotalRetrospect(String clubId, ClubTypeEnum clubRequiredActing, String opponendId) {
        Club club = this.clubRepository.findById(clubId).orElseThrow(() -> new NotFoundException(CLUB_NOT_FOUND));
        List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchRetrospect(club.getId(), clubRequiredActing, opponendId));
        return this.getClubRetrospectByOpponent(matches, clubId);
    }

    @Override
    @Transactional
    public ClubTotalRetrospectResponse getMatchRetrospect(String clubId, ClubTypeEnum clubRequiredActing) {
        Club club = this.clubRepository.findById(clubId).orElseThrow(() -> new NotFoundException(CLUB_NOT_FOUND));
        List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchRetrospect(club.getId(), clubRequiredActing, null));
        return this.getTotalClubRetrospect(matches, clubId);
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
            List<Match> matches = this.matchRepository.findAll(MatchSpecification.matchRetrospect(club.getId(), null, null));

            if(matches.isEmpty()) {
                continue;
            }

            ClubTotalRetrospectResponse totalClubRetrospect = getTotalClubRetrospect(matches, club.getId());
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

    private ClubTotalRetrospectResponse getTotalClubRetrospect(List<Match> matches, String clubId) {
        ClubTotalRetrospectResponse clubTotalRetrospectResponse = new ClubTotalRetrospectResponse();

        for(Match match : matches) {
            boolean playedAtHome = match.getHomeClub().getId().equals(clubId);

            int goalsConceded = playedAtHome ? match.getVisitingClubGoals() : match.getHomeClubGoals();
            int goalsScored = playedAtHome ? match.getHomeClubGoals() : match.getVisitingClubGoals();

            clubTotalRetrospectResponse.generateResult(goalsScored, goalsConceded);
        }

        return clubTotalRetrospectResponse;
    }


    private List<RetrospectByOpponentResponse> getClubRetrospectByOpponent(List<Match> matches, String clubId) {

        HashMap<String, RetrospectByOpponentResponse> totalRetrospect = new HashMap<>();

        for(Match match : matches) {
            boolean playedAtHome = match.getHomeClub().getId().equals(clubId);

            String opponentId = playedAtHome ? match.getVisitingClub().getId() : match.getHomeClub().getId();
            String opponentName = playedAtHome ? match.getVisitingClub().getName() : match.getHomeClub().getName();

            int goalsConceded = playedAtHome ? match.getVisitingClubGoals() : match.getHomeClubGoals();
            int goalsScored = playedAtHome ? match.getHomeClubGoals() : match.getVisitingClubGoals();

            totalRetrospect.computeIfAbsent(opponentId, _ -> new RetrospectByOpponentResponse(new Opponent(opponentId, opponentName)))
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
            match = this.matchRepository.findById(matchUpdateRequest.id())
                    .orElseThrow(() -> new NotFoundException(MATCH_NOT_FOUND));
        }

        Stadium stadium = this.stadiumRepository.findById(matchRequest.stadiumId())
                .orElseThrow(() -> new InvalidFieldsException(STADIUM_NOT_FOUND));

        Club visitingClub = this.clubRepository.findById(matchRequest.visitingClubResult().id())
                .orElseThrow(() -> new InvalidFieldsException(CLUB_NOT_FOUND));

        Club homeClub = this.clubRepository.findById(matchRequest.homeClubResult().id()).
                orElseThrow(() -> new InvalidFieldsException(CLUB_NOT_FOUND));

        return MatchMapper.toEntity(matchRequest, stadium, homeClub, visitingClub, match);

    }
}
