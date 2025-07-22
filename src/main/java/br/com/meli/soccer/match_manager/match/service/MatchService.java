package br.com.meli.soccer.match_manager.match.service;

import br.com.meli.soccer.match_manager.match.dto.GeneralRetrospect;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchActingFilter;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchThrashingFilter;
import br.com.meli.soccer.match_manager.match.dto.response.*;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchService {

    MatchResponse create(MatchCreateRequest matchCreateRequest);

    MatchResponse update(MatchUpdateRequest matchUpdateRequest);

    MatchResponse getById(String id);

    List<MatchResponse> getAll(String clubId, MatchThrashingFilter matchThrashingFilter, Pageable pageable);

    void deleteById(String id);

    List<RetrospectByOpponent> getRetrospectByOpponents(String clubId, MatchActingFilter matchActingFilter);

    GeneralRetrospect getGeneralRetrospect(String clubId, MatchActingFilter matchActingFilter);

    DirectConfrontationsResponse getDirectConfrontations(String idClubA, String idClubB);

    List<RankingResponse> getRanking();
}
