package br.com.meli.soccer.match_manager.stadium.controller;

import br.com.meli.soccer.match_manager.factory.StadiumDataFactory;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumCreateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumUpdateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import br.com.meli.soccer.match_manager.stadium.repository.StadiumRepository;
import br.com.meli.soccer.match_manager.stadium.service.StadiumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StadiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StadiumService stadiumService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StadiumRepository stadiumRepository;

    private StadiumDataFactory stadiumDataFactory;

    private StadiumResponse arenaPampa;
    private StadiumResponse horizonteAzul;
    private StadiumResponse solarDasPalmeiras;

    private final String baseUrl = "/stadium";

    @BeforeEach
    void setUp() {
        stadiumDataFactory = new StadiumDataFactory(stadiumService);
        stadiumRepository.deleteAll();
        fillDatabase();
    }

    @ParameterizedTest
    @MethodSource("invalidStadiumCreateRequestFields")
    void test_shouldCreateStadiumWithInvalidFields_and_return_400(StadiumCreateRequest stadiumCreateRequest) throws Exception {
        String stadiumCreateRequestJson = objectMapper.writeValueAsString(stadiumCreateRequest);
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(stadiumCreateRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_shouldCreateStadium_and_return_200() throws Exception {
        String name = "aurorão";

        StadiumCreateRequest stadiumCreateRequest = new StadiumCreateRequest(name);

        String stadiumCreateRequestJson = objectMapper.writeValueAsString(stadiumCreateRequest);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(stadiumCreateRequestJson))
                .andExpect(status().isCreated())
                .andExpect(result -> {

                    StadiumResponse stadiumResponse = objectMapper.readValue(result.getResponse().getContentAsString(), StadiumResponse.class);

                    Assertions.assertEquals(name.toUpperCase(), stadiumResponse.name());
                });
    }

    @Test
    void test_shouldCreateStadiumDuplicated_and_return_409() throws Exception {
        StadiumCreateRequest stadiumCreateRequest = new StadiumCreateRequest(arenaPampa.name());

        String stadiumCreateRequestJson = objectMapper.writeValueAsString(stadiumCreateRequest);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(stadiumCreateRequestJson))
                .andExpect(status().isConflict());
    }

    @ParameterizedTest
    @MethodSource("invalidStadiumUpdateRequestFields")
    void test_shouldUpdateStadiumWithInvalidFields_and_return_400(StadiumUpdateRequest stadiumUpdateRequest) throws Exception {
        String stadiumUpdateRequestJson = objectMapper.writeValueAsString(stadiumUpdateRequest);
        mockMvc.perform(put(baseUrl).contentType(MediaType.APPLICATION_JSON).content(stadiumUpdateRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_shouldUpdateStadium_and_return_200() throws Exception {
        String name = "aurorão";

        StadiumUpdateRequest stadiumUpdateRequest = new StadiumUpdateRequest(arenaPampa.id(), name);

        String stadiumUpdateRequestJson = objectMapper.writeValueAsString(stadiumUpdateRequest);

        mockMvc.perform(put(baseUrl).contentType(MediaType.APPLICATION_JSON).content(stadiumUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(result -> {

                    StadiumResponse stadiumResponse = objectMapper.readValue(result.getResponse().getContentAsString(), StadiumResponse.class);

                    Assertions.assertEquals(name.toUpperCase(), stadiumResponse.name());
                });
    }

    @Test
    void test_shouldUpdateStadiumWithDuplicatedName_and_return_409() throws Exception {
        StadiumUpdateRequest stadiumUpdateRequest = new StadiumUpdateRequest(solarDasPalmeiras.id(), horizonteAzul.name());

        String stadiumCreateRequestJson = objectMapper.writeValueAsString(stadiumUpdateRequest);

        mockMvc.perform(put(baseUrl).contentType(MediaType.APPLICATION_JSON).content(stadiumCreateRequestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void test_shouldUpdateStadium_and_return_404() throws Exception {
        StadiumUpdateRequest stadiumUpdateRequest = new StadiumUpdateRequest(UUID.randomUUID().toString(), "aurorão");

        String stadiumCreateRequestJson = objectMapper.writeValueAsString(stadiumUpdateRequest);

        mockMvc.perform(put(baseUrl).contentType(MediaType.APPLICATION_JSON).content(stadiumCreateRequestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_shouldGetStadiumById_and_return_200() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", arenaPampa.id()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    StadiumResponse stadiumResponse = objectMapper.readValue(result.getResponse().getContentAsString(), StadiumResponse.class);
                    Assertions.assertEquals(stadiumResponse.name(), arenaPampa.name());
                });
    }

    @Test
    void test_shouldGetStadiumById_and_return_404() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_shouldGetAllStadiums_and_return_200() throws Exception {
        mockMvc.perform(get(baseUrl + "/findAll"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    StadiumResponse[] stadiumResponsesDTO = objectMapper.readValue(result.getResponse().getContentAsString(), StadiumResponse[].class);
                    Assertions.assertEquals(3, stadiumResponsesDTO.length);
                });
    }

    @Test
    void test_shouldGetAllStadiumsByName_and_return_200() throws Exception {
    mockMvc.perform(get(baseUrl + "/findAll").param("name", solarDasPalmeiras.name()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    StadiumResponse[] stadiumResponsesDTO = objectMapper.readValue(result.getResponse().getContentAsString(), StadiumResponse[].class);
                    Assertions.assertEquals(1, stadiumResponsesDTO.length);
                });
    }

    @Test
    void test_shouldGetAllStadiumsByName_and_return_200_empty() throws Exception {
        mockMvc.perform(get(baseUrl + "/findAll").param("name", "maracanã"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    StadiumResponse[] stadiumResponsesDTO = objectMapper.readValue(result.getResponse().getContentAsString(), StadiumResponse[].class);
                    Assertions.assertEquals(0, stadiumResponsesDTO.length);
                });
    }

    static Stream<StadiumCreateRequest> invalidStadiumCreateRequestFields() {
        return Stream.of(
                new StadiumCreateRequest(""),
                new StadiumCreateRequest("a"),
                new StadiumCreateRequest("a".repeat(2)),
                new StadiumCreateRequest("a".repeat(51))
        );
    }

    static Stream<StadiumUpdateRequest> invalidStadiumUpdateRequestFields() {
        return Stream.of(
                new StadiumUpdateRequest("123",""),
                new StadiumUpdateRequest("123","a"),
                new StadiumUpdateRequest("123", "a".repeat(2)),
                new StadiumUpdateRequest("123", "a".repeat(51)),
                new StadiumUpdateRequest("", "aurorão")
        );
    }

    void fillDatabase() {
        arenaPampa = stadiumDataFactory.createArenaPampa();
        horizonteAzul = stadiumDataFactory.createHorizonteAzul();
        solarDasPalmeiras = stadiumDataFactory.createSolarDasPalmeiras();
    }
}
