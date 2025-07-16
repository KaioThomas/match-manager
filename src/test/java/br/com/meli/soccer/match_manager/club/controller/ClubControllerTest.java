package br.com.meli.soccer.match_manager.club.controller;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.request.ClubUpdateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponseDTO;
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

    private ClubResponseDTO gremio;
    private ClubResponseDTO gremio2;
    private ClubResponseDTO atletico;
    private ClubResponseDTO uniaoLitoranea;
    private ClubResponseDTO fenixMetropolitana;

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
        ClubCreateRequest clubRequest = new ClubCreateRequest("são paulo", AcronymStatesEnum.SP.toString(), LocalDate.now(), true);
        String clubRequestJson = objectMapper.writeValueAsString(clubRequest);

        mockMvc.perform(post("/club").contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(
                    status().isCreated()
                )
                .andExpect(result -> {
                    ClubResponseDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), ClubResponseDTO.class);
                    Assertions.assertEquals(response.name(), clubRequest.name().toUpperCase());
                    Assertions.assertEquals(response.acronymState(), clubRequest.stateAcronym().toUpperCase());
                    Assertions.assertEquals(response.creationDate(), clubRequest.creationDate());
                    Assertions.assertEquals(clubRequest.active(), response.active());
                });
    }

    @Test
    void test_shouldCreateDuplicatedClubs_and_return_409() throws Exception {
        ClubCreateRequest clubRequest = new ClubCreateRequest(gremio.name(), gremio.acronymState(), LocalDate.now(), true);
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
        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest(uniaoLitoranea.id(), uniaoLitoranea.name() + " litoral clube", AcronymStatesEnum.CE.toString(), uniaoLitoranea.creationDate().minusYears(1), !uniaoLitoranea.active());

        String clubUpdateRequestJson = objectMapper.writeValueAsString(clubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ClubResponseDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), ClubResponseDTO.class);
                    Assertions.assertEquals(response.name(), clubUpdateRequest.name().toUpperCase());
                    Assertions.assertEquals(response.acronymState(), clubUpdateRequest.stateAcronym().toUpperCase());
                    Assertions.assertEquals(response.creationDate(), clubUpdateRequest.creationDate());
                    Assertions.assertEquals(clubUpdateRequest.active(), response.active());
                });
    }

    @Test
    void test_shouldUpdateClub_and_return_404() throws Exception {

        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest(UUID.randomUUID().toString(), "palmeiras", AcronymStatesEnum.CE.toString(), LocalDate.of(2004, 6, 19), false);
        String clubUpdateRequestJson = objectMapper.writeValueAsString(clubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubUpdateRequestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_shouldUpdateDuplicatedClubs_and_return_409() throws Exception {
        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest(atletico.id(), gremio2.name(), gremio2.acronymState(), atletico.creationDate(), atletico.active());
        String clubUpdateRequestJson = objectMapper.writeValueAsString(clubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubUpdateRequestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void test_shouldSoftDeleteClub_and_return_204() throws Exception {
        mockMvc.perform(delete("/club/{id}", fenixMetropolitana.id())).andExpect(status().isNoContent());

        MvcResult deletedClubMvcResult = mockMvc.perform(get("/club/{id}", fenixMetropolitana.id())).andReturn();

        ClubResponseDTO deletedClubResponse = objectMapper.readValue(deletedClubMvcResult.getResponse().getContentAsString(), ClubResponseDTO.class);

        Assertions.assertFalse(deletedClubResponse.active());
    }

    @Test
    void test_shouldGetClub_and_return_200() throws Exception {
        MvcResult getClubMvcResult = mockMvc.perform(get("/club/{id}", gremio.id())).andReturn();
        ClubResponseDTO getClubResponse = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponseDTO.class);

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
        MvcResult getClubMvcResult = mockMvc.perform(get("/club").param("name", "são paulo")).andReturn();
        ClubResponseDTO[] clubResponseDTOS = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponseDTO[].class);
        Assertions.assertEquals(0, clubResponseDTOS.length);
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

        MvcResult getClubMvcResult = mockMvc.perform(get("/club")
                .params(MultiValueMap.fromSingleValue(params))
        ).andReturn();
        ClubResponseDTO[] clubResponseDTOS = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponseDTO[].class);
        Assertions.assertEquals(expectedClubs, clubResponseDTOS.length);
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
                new ClubUpdateRequest("123","", "SP", LocalDate.now(), true),
                new ClubUpdateRequest("123","sao paulo", "", LocalDate.now(), true),
                new ClubUpdateRequest("123","sao paulo", "SP", null, true),
                new ClubUpdateRequest("123","sao paulo", "SP", LocalDate.now(), null),
                new ClubUpdateRequest("123","s", "SP", LocalDate.now(), true),
                new ClubUpdateRequest("123","s".repeat(51), "SP", LocalDate.now(), true),
                new ClubUpdateRequest("123","sao paulo", "WH", LocalDate.now(), true),
                new ClubUpdateRequest("123","sao paulo", "S", LocalDate.now(), true),
                new ClubUpdateRequest("123","sao paulo", "SP", LocalDate.now().plusDays(1), true),
                new ClubUpdateRequest("","sao paulo", "SP", LocalDate.now(), true)
        );
    }

    static Stream<ClubCreateRequest> invalidClubCreateRequest() {
        return Stream.of(
                new ClubCreateRequest("", "SP", LocalDate.now(), true),
                new ClubCreateRequest("sao paulo", "", LocalDate.now(), true),
                new ClubCreateRequest("sao paulo", "SP", null, true),
                new ClubCreateRequest("sao paulo", "SP", LocalDate.now(), null),
                new ClubCreateRequest("s", "SP", LocalDate.now(), true),
                new ClubCreateRequest("s".repeat(51), "SP", LocalDate.now(), true),
                new ClubCreateRequest("sao paulo", "WH", LocalDate.now(), true),
                new ClubCreateRequest("sao paulo", "S", LocalDate.now(), true),
                new ClubCreateRequest("sao paulo", "SP", LocalDate.now().plusDays(1), true)
        );
    }
}
