package br.com.meli.soccer.match_manager.club.controller;

import br.com.meli.soccer.match_manager.club.dto.request.ClubCreateRequest;
import br.com.meli.soccer.match_manager.club.dto.request.ClubUpdateRequest;
import br.com.meli.soccer.match_manager.club.dto.response.ClubResponseDTO;
import br.com.meli.soccer.match_manager.club.repository.ClubRepository;
import br.com.meli.soccer.match_manager.club.service.ClubService;
import br.com.meli.soccer.match_manager.common.enums.AcronymStatesEnum;
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

    @BeforeEach
    void setUp(){
        clubRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("invalidClubCreateRequest")
    void test_shouldReturnBadRequestForInvalidFieldsWhenTryCreateclub_and_return_400(ClubCreateRequest invalidClubCreateRequest) throws Exception {

        String clubRequestJson = objectMapper.writeValueAsString(invalidClubCreateRequest);

        mockMvc.perform(post("/club").contentType(MediaType.APPLICATION_JSON).content(clubRequestJson)).andExpect(status().isBadRequest());
    }

    @Test
    void test_shouldCreateClub_and_return_201() throws Exception {
        String name = "são paulo";
        String stateAcronym = AcronymStatesEnum.SP.toString();
        LocalDate creationDate = LocalDate.now();
        Boolean active = true;

        ClubCreateRequest clubRequest = new ClubCreateRequest(name, stateAcronym, creationDate, active);
        String clubRequestJson = objectMapper.writeValueAsString(clubRequest);

        mockMvc.perform(post("/club").contentType(MediaType.APPLICATION_JSON).content(clubRequestJson))
                .andExpect(
                    status().isCreated()
                )
                .andExpect(result -> {
                    ClubResponseDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), ClubResponseDTO.class);
                    Assertions.assertEquals(response.name(), name.toUpperCase());
                    Assertions.assertEquals(response.acronymState(), stateAcronym.toUpperCase());
                    Assertions.assertEquals(response.creationDate(), creationDate);
                    Assertions.assertEquals(active, response.active());
                });
    }

    @Test
    void test_shouldCreateDuplicatedClubs_and_return_400() throws Exception {
        String name = "são paulo";
        String stateAcronym = AcronymStatesEnum.SP.toString();
        LocalDate creationDate = LocalDate.now().plusDays(1);
        Boolean active = true;

        ClubCreateRequest clubRequest = new ClubCreateRequest(name, stateAcronym, creationDate, active);
        String clubRequestJson = objectMapper.writeValueAsString(clubRequest);

        mockMvc.perform(post("/club")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clubRequestJson)
        ).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidClubUpdateRequest")
    void test_shouldReturnBadRequestForInvalidFieldsWhenTryUpdateclub_and_return_400(ClubUpdateRequest invalidClubUpdateRequest) throws Exception {

        String clubRequestJson = objectMapper.writeValueAsString(invalidClubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubRequestJson)).andExpect(status().isBadRequest());
    }

    @Test
    void test_shouldUpdateClub_and_return_200() throws Exception {

        ClubResponseDTO createdClubResponse = setClubAtDatabase("são paulo", AcronymStatesEnum.SP, LocalDate.now(), true);

        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest(createdClubResponse.id(), "palmeiras", AcronymStatesEnum.CE.toString(), LocalDate.now().minusYears(5), false);

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

        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest("44971a4d-46ee-493a-a33c-47be158caad2", "palmeiras", AcronymStatesEnum.CE.toString(), LocalDate.now().minusYears(5), false);
        String clubUpdateRequestJson = objectMapper.writeValueAsString(clubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubUpdateRequestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_shouldUpdateDuplicatedClubs_and_return_409() throws Exception {
        ClubResponseDTO createdClubResponse = setClubAtDatabase("são paulo", AcronymStatesEnum.SP, LocalDate.now(), true);

        setClubAtDatabase("palmeiras", AcronymStatesEnum.CE, LocalDate.now().minusYears(5), false);

        ClubUpdateRequest clubUpdateRequest = new ClubUpdateRequest(createdClubResponse.id(), "palmeiras", AcronymStatesEnum.CE.toString(), LocalDate.now(), true);
        String clubUpdateRequestJson = objectMapper.writeValueAsString(clubUpdateRequest);

        mockMvc.perform(put("/club").contentType(MediaType.APPLICATION_JSON).content(clubUpdateRequestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void test_shouldSoftDeleteClub_and_return_204() throws Exception {
        ClubResponseDTO createdClubResponse = setClubAtDatabase("são paulo", AcronymStatesEnum.SP, LocalDate.now(), true);

        mockMvc.perform(delete("/club/{id}", createdClubResponse.id())).andExpect(status().isNoContent());

        MvcResult deletedClubMvcResult = mockMvc.perform(get("/club/{id}", createdClubResponse.id())).andReturn();

        ClubResponseDTO deletedClubResponse = objectMapper.readValue(deletedClubMvcResult.getResponse().getContentAsString(), ClubResponseDTO.class);

        Assertions.assertFalse(deletedClubResponse.active());
    }

    @Test
    void test_shouldGetClub_and_return_200() throws Exception {
        ClubResponseDTO createdClubResponse = setClubAtDatabase("são paulo", AcronymStatesEnum.SP, LocalDate.now(), true);

        MvcResult getClubMvcResult = mockMvc.perform(get("/club/{id}", createdClubResponse.id())).andReturn();
        ClubResponseDTO getClubResponse = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponseDTO.class);

        Assertions.assertEquals(getClubResponse.name(), createdClubResponse.name().toUpperCase());
        Assertions.assertEquals(getClubResponse.acronymState(), createdClubResponse.acronymState().toUpperCase());
        Assertions.assertEquals(getClubResponse.creationDate(), createdClubResponse.creationDate());
        Assertions.assertEquals(createdClubResponse.active(), getClubResponse.active());
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
        MvcResult getClubMvcResult = mockMvc.perform(get("/club")).andReturn();
        ClubResponseDTO[] clubResponseDTOS = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponseDTO[].class);
        Assertions.assertEquals(0, clubResponseDTOS.length);
    }

     ClubResponseDTO setClubAtDatabase(String name, AcronymStatesEnum acronymState, LocalDate localDate, Boolean active) {
        ClubCreateRequest clubCreateRequest = new ClubCreateRequest(name, acronymState.toString(), localDate, active);
        return clubService.create(clubCreateRequest);
    }

    @ParameterizedTest
    @MethodSource("searchClubByFilters")
    void test_shouldGetAllByFilters_and_return_200(Map<String, String> params, int expectedClubs) throws Exception {
        setClubAtDatabase("são paulo", AcronymStatesEnum.SP, LocalDate.now().minusYears(10), true);
        setClubAtDatabase("são paulo", AcronymStatesEnum.AL, LocalDate.now().minusYears(20), true);
        setClubAtDatabase("palmeiras", AcronymStatesEnum.CE, LocalDate.now().minusYears(30).minusMonths(13), false);
        setClubAtDatabase("red bull", AcronymStatesEnum.SP, LocalDate.now().minusDays(10), true);

        MvcResult getClubMvcResult = mockMvc.perform(get("/club")
                .params(MultiValueMap.fromSingleValue(params))
        ).andReturn();
        ClubResponseDTO[] clubResponseDTOS = objectMapper.readValue(getClubMvcResult.getResponse().getContentAsString(), ClubResponseDTO[].class);
        Assertions.assertEquals(expectedClubs, clubResponseDTOS.length);
    }


    static Stream<Arguments> searchClubByFilters() {
        Map<String, String> paramsByName = Map.of("name", "são paulo");

        Map<String, String> paramsByActive = Map.of("active", "false");

        Map<String, String> paramsByAcronymState = Map.of("acronymState", "CE");

        Map<String, String> paramsByAcronymStateNameAndActive = Map.of(
                "acronymState", "AL",
                "name", "são paulo",
                "active", "true"
        );

        return Stream.of(
                Arguments.of(paramsByName, 2),
                Arguments.of(paramsByActive, 1),
                Arguments.of(paramsByAcronymState, 1),
                Arguments.of(paramsByAcronymStateNameAndActive, 1),
                Arguments.of(Map.of(), 4)
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
