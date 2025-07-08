package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.model.dto.request.StadiumRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.StadiumResponseDTO;
import br.com.meli.soccer.match_manager.model.entity.Stadium;
import br.com.meli.soccer.match_manager.model.exception.NotFoundException;
import br.com.meli.soccer.match_manager.repository.StadiumRepository;
import br.com.meli.soccer.match_manager.service.StadiumService;
import br.com.meli.soccer.match_manager.service.converter.StadiumConverter;
import br.com.meli.soccer.match_manager.service.validator.StadiumValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;
    private final StadiumValidator stadiumValidator;

    @Override
    @Transactional
    public StadiumResponseDTO create(StadiumRequestDTO stadiumRequestDTO) {
        Stadium stadium = StadiumConverter.toEntity(stadiumRequestDTO);

        this.stadiumValidator.validateCreate(stadium);

        Stadium response = this.stadiumRepository.save(stadium);
        return StadiumConverter.toResponseDTO(response);
    }

    @Override
    @Transactional
    public StadiumResponseDTO update(StadiumRequestDTO stadiumRequestDTO, String id) {
        Stadium stadium = this.stadiumRepository.findById(id).orElseThrow(() -> new NotFoundException("Stadium was not found"));
        stadium.setName(stadiumRequestDTO.name().toUpperCase());

        this.stadiumValidator.validateStadiumWithSameName(stadium);

        Stadium response = this.stadiumRepository.save(stadium);
        return StadiumConverter.toResponseDTO(response);
    }

    @Override
    @Transactional
    public StadiumResponseDTO getById(String id) {
        Stadium response = this.stadiumRepository.findById(id).orElseThrow(() -> new NotFoundException("Stadium was not found"));

        return StadiumConverter.toResponseDTO(response);
    }

    @Override
    @Transactional
    public List<StadiumResponseDTO> getAll(String name, Pageable pageable) {
        Stadium stadium = new Stadium();
        stadium.setName(name);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Stadium> stadiumExample = Example.of(stadium, exampleMatcher);
        List<Stadium> stadiums = this.stadiumRepository.findAll(stadiumExample, pageable).getContent();

        return stadiums.stream()
                .map(StadiumConverter::toResponseDTO)
                .toList();
    }
}
