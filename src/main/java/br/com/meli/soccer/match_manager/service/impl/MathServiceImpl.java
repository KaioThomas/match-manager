package br.com.meli.soccer.match_manager.service.impl;

import br.com.meli.soccer.match_manager.model.dto.request.MatchRequestDTO;
import br.com.meli.soccer.match_manager.model.dto.response.MatchResponseDTO;
import br.com.meli.soccer.match_manager.model.entity.Club;
import br.com.meli.soccer.match_manager.model.entity.Match;
import br.com.meli.soccer.match_manager.model.entity.Stadium;
import br.com.meli.soccer.match_manager.model.exception.InvalidFieldsException;
import br.com.meli.soccer.match_manager.repository.ClubRepository;
import br.com.meli.soccer.match_manager.repository.MatchRepository;
import br.com.meli.soccer.match_manager.repository.StadiumRepository;
import br.com.meli.soccer.match_manager.service.MatchService;
import br.com.meli.soccer.match_manager.service.converter.MatchConverter;
import br.com.meli.soccer.match_manager.service.validator.MatchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MathServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final ClubRepository clubRepository;
    private final StadiumRepository stadiumRepository;

    private final MatchValidator matchValidator;

    @Override
    public MatchResponseDTO create(MatchRequestDTO matchRequestDTO) {
        Club homeClub = this.clubRepository.findById(matchRequestDTO.homeClubId()).orElseThrow(() -> new InvalidFieldsException("The club is invalid"));
        Club visitingClub = this.clubRepository.findById(matchRequestDTO.visitingClubId()).orElseThrow(() -> new InvalidFieldsException("The club is invalid"));
        Stadium stadium = this.stadiumRepository.findById(matchRequestDTO.stadiumId()).orElseThrow(() -> new InvalidFieldsException("The stadium is invalid"));
        Match match = MatchConverter.toEntity(matchRequestDTO, stadium, homeClub, visitingClub);
        this.matchValidator.validate(match);
        return MatchConverter.toResponseDTO(this.matchRepository.save(match));
    }

    @Override
    public MatchResponseDTO update(MatchRequestDTO matchRequestDTO) {
        return null;
    }

    @Override
    public MatchResponseDTO getById(UUID id) {
        return null;
    }

    @Override
    public List<MatchResponseDTO> getAll(UUID clubId, UUID stadiumId, Integer pageNumber, Integer size, String orderBy, String direction) {
        return List.of();
    }

    @Override
    public void deleteById(UUID id) {

    }
}
