package br.com.meli.soccer.match_manager.club.controller;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.request.ClubUpdateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponse;
import br.com.meli.soccer.match_manager.club.repository.ClubRepository;
import br.com.meli.soccer.match_manager.club.service.ClubService;
import br.com.meli.soccer.match_manager.common.enums.AcronymStatesEnum;
import br.com.meli.soccer.match_manager.factory.ClubDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubService clubService;

    private ClubDataFactory clubDataFactory;

    private ClubResponse gremio;
    private ClubResponse gremio2;
    private ClubResponse atletico;
    private ClubResponse uniaoLitoranea;
    private ClubResponse fenixMetropolitana;

    @BeforeEach
    void setUp(){
        clubDataFactory = new ClubDataFactory(clubService);
        clubRepository.deleteAll();
        fillDatabase();
    }

    @ParameterizedTest
    @MethodSource("invalidClubCreateRequest")
    void test_shouldReturnBadRequestForInvalidFieldsWhenTryCreateclub_and_return_400(ClubCreateRequest invalidClubCreateRequest) throws Exception {

        String clubRequestJson = objectMapper.writeValueAsString(invalidClubCreateRequest);

        mockMvc.perform(post("/club").contentType(MediaType.APPLICATION_JSON).content(clubRequestJson)).andExpect(status().isBadRequest());
    }

    @Test
    void test_shouldCreateClub_and_return_201() throws Exception {
        ClubCreateRequest clubRequest = new ClubCreateRequest("são paulo", AcronymStatesEnum.SP, LocalDate.now(), true);
        String clubRequestJson = objectMapper.writeValueAsString(clubRequest);

        mockMvc.perform(post("/club").contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(
                    status().isCreated()
                )
                .andExpect(result -> {
                    ClubResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ClubResponse.class);
                    Assertions.assertEquals(response.name(), clubRequest.getName().toUpperCase());
                    Assertions.assertEquals(response.acronymState(), clubRequest.getStateAcronym().toString());
                    Assertions.assertEquals(response.creationDate(), clubRequest.getCreationDate());
                    Assertions.assertEquals(clubRequest.getActive(), response.active());
                });
    }

    @Test
    void test_shouldCreateDuplicatedClubs_and_return_409() throws Exception {
        ClubCreateRequest clubRequest = new ClubCreateRequest(gremio.name(), AcronymStatesEnum.valueOf(gremio.acronymState()), LocalDate.now(), true);
        String clubRequestJson = objectMapper.writeValueAsString(clubRequest);

        mockMvc.perform(post("/club")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clubRequestJson)
        ).andExpect(status().isConflict());
    }

    @ParameterizedTest
    @MethodSource("invalidClubUpdateRequest")
    void test_shouldReturnBadRequestForInvalidFieldsWhenTryUpdateclub_and_return_400(ClubUpdateRequest invalidClubUpdateRequest) throws Exception {

        String clubRequestJson = objectMapper.writeValueAsString(invalidClubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubRequestJson)).andExpect(status().isBadRequest());
    }

    @Test
    void test_shouldUpdateClub_and_return_200() throws Exception {
        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest(uniaoLitoranea.id(), uniaoLitoranea.name() + " litoral clube", AcronymStatesEnum.CE, uniaoLitoranea.creationDate().minusYears(1), !uniaoLitoranea.active());

        String clubUpdateRequestJson = objectMapper.writeValueAsString(clubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ClubResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ClubResponse.class);
                    Assertions.assertEquals(response.name(), clubUpdateRequest.getName().toUpperCase());
                    Assertions.assertEquals(response.acronymState(), clubUpdateRequest.getStateAcronym().toString());
                    Assertions.assertEquals(response.creationDate(), clubUpdateRequest.getCreationDate());
                    Assertions.assertEquals(clubUpdateRequest.getActive(), response.active());
                });
    }

    @Test
    void test_shouldUpdateClub_and_return_404() throws Exception {

        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest(UUID.randomUUID().toString(), "palmeiras", AcronymStatesEnum.CE, LocalDate.of(2004, 6, 19), false);
        String clubUpdateRequestJson = objectMapper.writeValueAsString(clubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubUpdateRequestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_shouldUpdateDuplicatedClubs_and_return_409() throws Exception {
        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest(atletico.id(), gremio2.name(), AcronymStatesEnum.valueOf(gremio2.acronymState()), atletico.creationDate(), atletico.active());
        String clubUpdateRequestJson = objectMapper.writeValueAsString(clubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubUpdateRequestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void test_shouldSoftDeleteClub_and_return_204() throws Exception {
        mockMvc.perform(delete("/club/{id}", fenixMetropolitana.id())).andExpect(status().isNoContent());

        MvcResult deletedClubMvcResult = mockMvc.perform(get("/club/{id}", fenixMetropolitana.id())).andReturn();

        ClubResponse deletedClubResponse = objectMapper.readValue(deletedClubMvcResult.getResponse().getContentAsString(), ClubResponse.class);

        Assertions.assertFalse(deletedClubResponse.active());
    }

    @Test
    void test_shouldGetClub_and_return_200() throws Exception {
        MvcResult getClubMvcResult = mockMvc.perform(get("/club/{id}", gremio.id())).andReturn();
        ClubResponse getClubResponse = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponse.class);

        Assertions.assertEquals(getClubResponse.name(), gremio.name().toUpperCase());
        Assertions.assertEquals(getClubResponse.acronymState(), gremio.acronymState().toUpperCase());
        Assertions.assertEquals(getClubResponse.creationDate(), gremio.creationDate());
        Assertions.assertEquals(gremio.active(), getClubResponse.active());
    }

    @Test
    void test_shouldGetClub_and_return_404() throws Exception {
        mockMvc.perform(get("/club/{id}", "44971a4d-46ee-493a-a33c-47be158caad2")).andExpect(status().isNotFound());

    }

    @Test
    void test_shouldSoftDeleteClub_and_return_404() throws Exception {
        mockMvc.perform(delete("/club/{id}", "44971a4d-46ee-493a-a33c-47be158caad2")).andExpect(status().isNotFound());
    }

    @Test
    void test_shouldGetAll_and_return_200_empty_list() throws Exception {
        MvcResult getClubMvcResult = mockMvc.perform(get("/club/findAll").param("name", "são paulo")).andReturn();
        ClubResponse[] clubResponses = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponse[].class);
        Assertions.assertEquals(0, clubResponses.length);
    }

    void fillDatabase() {
        gremio = clubDataFactory.createGremio();
        gremio2 = clubDataFactory.createGremio2();
        atletico = clubDataFactory.createAtletico();
        uniaoLitoranea = clubDataFactory.createUniaoLitoranea();
        fenixMetropolitana = clubDataFactory.createFenixMetropolitana();
    }

    @ParameterizedTest
    @MethodSource("searchClubByFilters")
    void test_shouldGetAllByFilters_and_return_200(Map<String, String> params, int expectedClubs) throws Exception {

        MvcResult getClubMvcResult = mockMvc.perform(get("/club/findAll")
                .params(MultiValueMap.fromSingleValue(params))
        ).andReturn();
        ClubResponse[] clubResponses = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponse[].class);
        Assertions.assertEquals(expectedClubs, clubResponses.length);
    }

    static Stream<Arguments> searchClubByFilters() {
        Map<String, String> paramsByName = Map.of("name", "grêmio");

        Map<String, String> paramsByActive = Map.of("active", "false");

        Map<String, String> paramsByAcronymState = Map.of("acronymState", AcronymStatesEnum.SP.toString());

        Map<String, String> paramsByAcronymStateNameAndActive = Map.of(
                "acronymState", AcronymStatesEnum.GO.toString(),
                "name", "grêmio",
                "active", "true"
        );

        return Stream.of(
                Arguments.of(paramsByName, 2),
                Arguments.of(paramsByActive, 1),
                Arguments.of(paramsByAcronymState, 2),
                Arguments.of(paramsByAcronymStateNameAndActive, 1),
                Arguments.of(Map.of(), 5)
        );
    }

    static Stream<ClubUpdateRequest> invalidClubUpdateRequest() {
        return Stream.of(
                new ClubUpdateRequest("123","", AcronymStatesEnum.SP, LocalDate.now(), true),
                new ClubUpdateRequest("123","sao paulo", AcronymStatesEnum.SP, null, true),
                new ClubUpdateRequest("123","sao paulo", AcronymStatesEnum.SP, LocalDate.now(), null),
                new ClubUpdateRequest("123","s", AcronymStatesEnum.SP, LocalDate.now(), true),
                new ClubUpdateRequest("123","s".repeat(51), AcronymStatesEnum.SP, LocalDate.now(), true),
                new ClubUpdateRequest("123","sao paulo", AcronymStatesEnum.SP, LocalDate.now().plusDays(1), true),
                new ClubUpdateRequest("","sao paulo", AcronymStatesEnum.SP, LocalDate.now(), true)
        );
    }

    static Stream<ClubCreateRequest> invalidClubCreateRequest() {
        return Stream.of(
                new ClubCreateRequest("", AcronymStatesEnum.SP, LocalDate.now(), true),
                new ClubCreateRequest("sao paulo", AcronymStatesEnum.SP, null, true),
                new ClubCreateRequest("sao paulo", AcronymStatesEnum.SP, LocalDate.now(), null),
                new ClubCreateRequest("s", AcronymStatesEnum.SP, LocalDate.now(), true),
                new ClubCreateRequest("s".repeat(51), AcronymStatesEnum.SP, LocalDate.now(), true),
                new ClubCreateRequest("sao paulo", AcronymStatesEnum.SP, LocalDate.now().plusDays(1), true)
        );
    }
}
