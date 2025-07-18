package br.com.meli.soccer.match_manager.stadium.service.impl;

import br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumCreateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumUpdateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import br.com.meli.soccer.match_manager.stadium.entity.Stadium;
import br.com.meli.soccer.match_manager.common.exception.NotFoundException;
import br.com.meli.soccer.match_manager.stadium.repository.StadiumRepository;
import br.com.meli.soccer.match_manager.stadium.service.StadiumService;
import br.com.meli.soccer.match_manager.stadium.mapper.StadiumMapper;
import br.com.meli.soccer.match_manager.stadium.validation.StadiumValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.meli.soccer.match_manager.common.constants.ValidationFailedMessageConstants.STADIUM;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;
    private final StadiumValidator stadiumValidator;

    @Override
    @Transactional
    public StadiumResponse create(StadiumCreateRequest stadiumCreateRequest) {
        Stadium stadium = StadiumMapper.toEntity(stadiumCreateRequest, new Stadium());

        this.stadiumValidator.validateCreate(stadium);

        Stadium response = this.stadiumRepository.save(stadium);
        return StadiumMapper.toResponseDTO(response);
    }

    @Override
    @Transactional
    public StadiumResponse update(StadiumUpdateRequest stadiumUpdateRequest) {
        Stadium oldStadium = this.stadiumRepository.findById(stadiumUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException(ValidationFailedMessageConstants.STADIUM.NOT_FOUND));

        Stadium updatedStadium = StadiumMapper.toEntity(stadiumUpdateRequest, oldStadium);

        this.stadiumValidator.validateStadiumWithSameName(updatedStadium);

        Stadium response = this.stadiumRepository.save(updatedStadium);
        return StadiumMapper.toResponseDTO(response);
    }

    @Override
    @Transactional
    public StadiumResponse getById(String id) {
        Stadium response = this.stadiumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(STADIUM.NOT_FOUND));

        return StadiumMapper.toResponseDTO(response);
    }

    @Override
    @Transactional
    public List<StadiumResponse> getAll(String name, Pageable pageable) {
        Stadium stadium = new Stadium();
        stadium.setName(name);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Stadium> stadiumExample = Example.of(stadium, exampleMatcher);
        List<Stadium> stadiums = this.stadiumRepository.findAll(stadiumExample, pageable).getContent();

        return stadiums.stream()
                .map(StadiumMapper::toResponseDTO)
                .toList();
    }
}
