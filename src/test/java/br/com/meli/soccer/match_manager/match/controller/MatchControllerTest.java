package br.com.meli.soccer.match_manager.match.controller;

import br.com.meli.soccer.match_manager.club.dto.request.ClubResult;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.club.service.ClubService;
import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import br.com.meli.soccer.match_manager.factory.ClubDataFactory;
import br.com.meli.soccer.match_manager.factory.MatchDataFactory;
import br.com.meli.soccer.match_manager.factory.StadiumDataFactory;
import br.com.meli.soccer.match_manager.match.dto.request.MatchCreateRequest;
import br.com.meli.soccer.match_manager.match.dto.request.MatchUpdateRequest;
import br.com.meli.soccer.match_manager.match.dto.response.MatchResponse;
import br.com.meli.soccer.match_manager.match.repository.MatchRepository;
import br.com.meli.soccer.match_manager.match.service.MatchService;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import br.com.meli.soccer.match_manager.stadium.service.StadiumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClubService clubService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private StadiumService stadiumService;

    @Autowired
    private MatchRepository matchRepository;

    private ClubDataFactory clubDataFactory;

    private StadiumDataFactory stadiumDataFactory;

    private MatchDataFactory matchDataFactory;

    private final String basePath = "/match";

    @BeforeAll
    void setUp(){
        clubDataFactory = new ClubDataFactory(clubService);
        matchDataFactory = new MatchDataFactory(matchService);
        stadiumDataFactory = new StadiumDataFactory(stadiumService);

        matchRepository.deleteAll();
        fillDatabase();
    }

    private ClubResponse gremioClub;
    private ClubResponse gremio2Club;
    private ClubResponse atleticoClub;
    private ClubResponse fenixMetropolitanaClub;
    private ClubResponse uniaoLitoraneaClub;

    private StadiumResponse arenaPampaStadium;
    private StadiumResponse horizonteAzulStadium;
    private StadiumResponse solarDasPalmeirasStadium;

    private MatchResponse atleticoVsGremioAtArenaPampaMatch;
    private MatchResponse gremioAVsAtleticoAtArenaPampaMatch;
    private MatchResponse fenixVsGremio2AtArenaPampaMatch;
    private MatchResponse fenixVsAtleticoAtSolarDasPalmeirasMatch;

    @Test
    void test_shouldGetAMatch_and_return_200() throws Exception {
        mockMvc.perform(get(basePath + "/{id}", fenixVsAtleticoAtSolarDasPalmeirasMatch.id())).andExpect(status().isOk());
    }

    @Test
    void test_shouldTryToGetAMatchThatDoesNotExists_and_return_404() throws Exception {
        mockMvc.perform(get(basePath + "/{id}", UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("searchMatchByFilters")
    void test_shouldGetAllByFilters_and_return_200(Map<String, String> params, int expectedMatches) throws Exception {

        MvcResult getClubMvcResult = mockMvc.perform(get(basePath + "/findAll")
                .params(MultiValueMap.fromSingleValue(params))
        ).andReturn();
        MatchResponse[] matchResponse = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), MatchResponse[].class);
        Assertions.assertEquals(expectedMatches, matchResponse.length);
    }

    @Test
    void test_shouldGetAllRetrospectByOpponent_and_return_200() throws Exception {

        mockMvc.perform(get(basePath + "/retrospect/opponents")
                .param("clubId", atleticoClub.id())
                .param("opponentId", gremioClub.id())
                .param("clubRequiredActing", ClubTypeEnum.HOME.toString())
        ).andExpect(status().isOk());
    }

    @Test
    void test_shouldGetAllRetrospectByOpponentWithAClubthatDoesnotExists_and_return_404() throws Exception {

        mockMvc.perform(get(basePath + "/retrospect/opponents")
                .param("clubId", UUID.randomUUID().toString())
                .param("opponentId", gremioClub.id())
        ).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("invalidMatchCreateRequest")
    void test_shouldTryToCreateAInvalidMatch_and_return_400(MatchCreateRequest invalidMatchCreateRequest) throws Exception {

        String clubRequestJson = objectMapper.writeValueAsString(invalidMatchCreateRequest);

        mockMvc.perform(post(basePath).contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("conflictsMatchCreateRequest")
    void test_shouldTryToCreateAConflictMatch_and_return_409(MatchCreateRequest conflictMatchRequest) throws Exception {

        String clubRequestJson = objectMapper.writeValueAsString(conflictMatchRequest);

        mockMvc.perform(post(basePath).contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void test_shouldCreateMatch_and_return_201() throws Exception {
        MatchCreateRequest matchCreateRequest = new MatchCreateRequest(new ClubResult(atleticoClub.id(), 2), new ClubResult(gremio2Club.id(), 4), arenaPampaStadium.id(), LocalDateTime.of(2018, 4, 4, 8, 0));
        String clubRequestJson = objectMapper.writeValueAsString(matchCreateRequest);

        mockMvc.perform(post(basePath).contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    MatchResponse matchResponse =  objectMapper.readValue(result.getResponse().getContentAsString(), MatchResponse.class);

                    Assertions.assertEquals(matchResponse.dateTime(), matchCreateRequest.getDateTime());
                    Assertions.assertEquals(matchResponse.stadium().id(), matchCreateRequest.getStadiumId());
                    Assertions.assertEquals(matchResponse.homeClub().id(), matchCreateRequest.getHomeClubResult().id());
                    Assertions.assertEquals(matchResponse.visitingClub().id(), matchCreateRequest.getVisitingClubResult().id());
                });
    }

    @ParameterizedTest
    @MethodSource("invalidMatchUpdateRequest")
    void test_shouldTryToUpdateAInvalidMatch_and_return_400(MatchUpdateRequest invalidMatchUpdateRequest) throws Exception {

        String clubRequestJson = objectMapper.writeValueAsString(invalidMatchUpdateRequest);

        mockMvc.perform(put(basePath).contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("conflictsMatchUpdateRequest")
    void test_shouldTryToUpdateAConflictMatch_and_return_409(MatchUpdateRequest conflictMatchRequest) throws Exception {

        String clubRequestJson = objectMapper.writeValueAsString(conflictMatchRequest);

        mockMvc.perform(put(basePath).contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void test_shouldTryToUpdateAMatchThatDoesNotExists_and_return_404() throws Exception {
        MatchUpdateRequest matchUpdateRequest = new MatchUpdateRequest(UUID.randomUUID().toString(), new ClubResult(atleticoClub.id(), 2), new ClubResult(gremio2Club.id(), 4), arenaPampaStadium.id(), LocalDateTime.of(2018, 4, 4, 8, 0));

        String clubRequestJson = objectMapper.writeValueAsString(matchUpdateRequest);

        mockMvc.perform(put(basePath).contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_shouldDeleteAMatch_and_return_204() throws Exception {
        mockMvc.perform(delete(basePath + "/{id}", fenixVsGremio2AtArenaPampaMatch.id())).andExpect(status().isNoContent());
    }
    @Test
    void test_shouldTryDeleteAMatchThatDoesNotExists_and_return_404() throws Exception {
        mockMvc.perform(delete(basePath + "/{id}", UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    Stream<MatchCreateRequest> conflictsMatchCreateRequest() {

        return Stream.of(
                new MatchCreateRequest(new ClubResult(atleticoClub.id(), 2), new ClubResult(gremio2Club.id(), 4), arenaPampaStadium.id(), LocalDateTime.of(1817, 4, 4, 8, 0)),
                new MatchCreateRequest(new ClubResult(uniaoLitoraneaClub.id(), 2), new ClubResult(gremio2Club.id(), 4), arenaPampaStadium.id(), LocalDateTime.of(2017, 4, 4, 8, 0)),
                new MatchCreateRequest(new ClubResult(atleticoClub.id(), 2), new ClubResult(gremioClub.id(), 4), horizonteAzulStadium.id(), atleticoVsGremioAtArenaPampaMatch.dateTime()),
                new MatchCreateRequest(new ClubResult(atleticoClub.id(), 2), new ClubResult(gremioClub.id(), 4), horizonteAzulStadium.id(), atleticoVsGremioAtArenaPampaMatch.dateTime().plusHours(47)),
                new MatchCreateRequest(new ClubResult(atleticoClub.id(), 2), new ClubResult(fenixMetropolitanaClub.id(), 4), arenaPampaStadium.id(), atleticoVsGremioAtArenaPampaMatch.dateTime())
         );
    }
    Stream<MatchUpdateRequest> conflictsMatchUpdateRequest() {

        return Stream.of(
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(atleticoClub.id(), 2), new ClubResult(gremio2Club.id(), 4), arenaPampaStadium.id(), LocalDateTime.of(1817, 4, 4, 8, 0)),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(uniaoLitoraneaClub.id(), 2), new ClubResult(gremio2Club.id(), 4), arenaPampaStadium.id(), LocalDateTime.of(2017, 4, 4, 8, 0)),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(atleticoClub.id(), 2), new ClubResult(gremioClub.id(), 4), horizonteAzulStadium.id(), atleticoVsGremioAtArenaPampaMatch.dateTime()),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(atleticoClub.id(), 2), new ClubResult(gremioClub.id(), 4), horizonteAzulStadium.id(), atleticoVsGremioAtArenaPampaMatch.dateTime().plusHours(47)),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(atleticoClub.id(), 2), new ClubResult(fenixMetropolitanaClub.id(), 4), arenaPampaStadium.id(), atleticoVsGremioAtArenaPampaMatch.dateTime())
         );
    }

    Stream<MatchCreateRequest> invalidMatchCreateRequest() {

        return Stream.of(
                new MatchCreateRequest(new ClubResult("", 4), new ClubResult(UUID.randomUUID().toString(), 1), UUID.randomUUID().toString(), LocalDateTime.now()),
                new MatchCreateRequest(new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult("", 1), UUID.randomUUID().toString(), LocalDateTime.now()),
                new MatchCreateRequest(new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult(UUID.randomUUID().toString(), 1), "", LocalDateTime.now()),
                new MatchCreateRequest(new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult(UUID.randomUUID().toString(), 1), UUID.randomUUID().toString(), null),
                new MatchCreateRequest(new ClubResult(UUID.randomUUID().toString(), -1), new ClubResult(UUID.randomUUID().toString(), 1), UUID.randomUUID().toString(), LocalDateTime.now()),
                new MatchCreateRequest(new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult(UUID.randomUUID().toString(), -1), UUID.randomUUID().toString(), LocalDateTime.now().plusDays(1)),
                new MatchCreateRequest(new ClubResult(atleticoClub.id(), 2), new ClubResult(atleticoClub.id(), 4), arenaPampaStadium.id(), LocalDateTime.of(2018, 4, 4, 8, 0)),
                new MatchCreateRequest(new ClubResult(gremio2Club.id(), 2), new ClubResult(atleticoClub.id(), 4), UUID.randomUUID().toString(), LocalDateTime.of(2020, 4, 4, 8, 0)),
                new MatchCreateRequest(new ClubResult(gremio2Club.id(), 2), new ClubResult(UUID.randomUUID().toString(), 4), arenaPampaStadium.id(), LocalDateTime.of(2020, 4, 4, 8, 0))
        );
    }

    Stream<Arguments> searchMatchByFilters() {

        Map<String, String> paramsByHomeClubId = Map.of("clubId", fenixMetropolitanaClub.id());

        return Stream.of(
                Arguments.of(paramsByHomeClubId, 2),
                Arguments.of(Map.of(), 4)
        );
    }

    Stream<MatchUpdateRequest> invalidMatchUpdateRequest() {

        return Stream.of(
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult("", 4), new ClubResult(UUID.randomUUID().toString(), 1), UUID.randomUUID().toString(), LocalDateTime.now()),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult("", 1), UUID.randomUUID().toString(), LocalDateTime.now()),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult(UUID.randomUUID().toString(), 1), "", LocalDateTime.now()),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult(UUID.randomUUID().toString(), 1), UUID.randomUUID().toString(), null),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(UUID.randomUUID().toString(), -1), new ClubResult(UUID.randomUUID().toString(), 1), UUID.randomUUID().toString(), LocalDateTime.now()),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult(UUID.randomUUID().toString(), -1), UUID.randomUUID().toString(), LocalDateTime.now().plusDays(1)),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(atleticoClub.id(), 2), new ClubResult(atleticoClub.id(), 4), arenaPampaStadium.id(), LocalDateTime.of(2018, 4, 4, 8, 0)),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(gremio2Club.id(), 2), new ClubResult(atleticoClub.id(), 4), UUID.randomUUID().toString(), LocalDateTime.of(2020, 4, 4, 8, 0)),
                new MatchUpdateRequest(gremioAVsAtleticoAtArenaPampaMatch.id(), new ClubResult(gremio2Club.id(), 2), new ClubResult(UUID.randomUUID().toString(), 4), arenaPampaStadium.id(), LocalDateTime.of(2020, 4, 4, 8, 0)),
                new MatchUpdateRequest("", new ClubResult(UUID.randomUUID().toString(), 4), new ClubResult(UUID.randomUUID().toString(), -1), UUID.randomUUID().toString(), LocalDateTime.now())
        );
    }

    void fillDatabase() {
        gremioClub = clubDataFactory.createGremio();
        gremio2Club = clubDataFactory.createGremio2();

        atleticoClub = clubDataFactory.createAtletico();
        fenixMetropolitanaClub = clubDataFactory.createFenixMetropolitana();
        uniaoLitoraneaClub = clubDataFactory.createUniaoLitoranea();

        arenaPampaStadium = stadiumDataFactory.createArenaPampa();
        horizonteAzulStadium = stadiumDataFactory.createHorizonteAzul();
        solarDasPalmeirasStadium = stadiumDataFactory.createSolarDasPalmeiras();

        atleticoVsGremioAtArenaPampaMatch = matchDataFactory.createMatch(atleticoClub, gremioClub, arenaPampaStadium, LocalDateTime.of(2010, 5, 6, 8, 0));
        gremioAVsAtleticoAtArenaPampaMatch = matchDataFactory.createMatch(gremioClub, atleticoClub, arenaPampaStadium, LocalDateTime.of(2015, 1, 3, 10, 0));
        fenixVsGremio2AtArenaPampaMatch = matchDataFactory.createMatch(fenixMetropolitanaClub, gremio2Club, arenaPampaStadium, LocalDateTime.of(2003, 5, 6, 8, 0));
        fenixVsAtleticoAtSolarDasPalmeirasMatch = matchDataFactory.createMatch(fenixMetropolitanaClub, atleticoClub, solarDasPalmeirasStadium, LocalDateTime.of(2003, 6, 6, 8, 0));
    }
}
