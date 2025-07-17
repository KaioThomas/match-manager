package br.com.meli.soccer.match_manager.match.service;

import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import br.com.meli.soccer.match_manager.match.dto.response.ClubTotalRetrospectResponse;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.filter.MatchFilterRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchHistoryResponse;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponse;
import br.com.meli.soccer.match_manager.match.dto.response.RetrospectByOpponentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchService {

    MatchResponse create(MatchCreateRequest matchCreateRequest);

    MatchResponse update(MatchUpdateRequest matchUpdateRequest);

    MatchResponse getById(String id);

    List<MatchResponse> getAll(MatchFilterRequest matchFilterRequest, Pageable pageable);

    void deleteById(String id);

    List<RetrospectByOpponentResponse> getTotalRetrospect(String id, ClubTypeEnum clubRequiredActing, String opponendId);

    MatchTotalRetrospect getMatchRetrospect(String id, ClubTypeEnum clubRequiredActing);
    ClubTotalRetrospectResponse getMatchRetrospect(String id, ClubTypeEnum clubRequiredActing);
}
