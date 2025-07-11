package br.com.meli.soccer.match_manager.stadium.validation;

import br.com.meli.soccer.match_manager.stadium.entity.Stadium;
import br.com.meli.soccer.match_manager.stadium.dto.specification.StadiumSpecification;
import br.com.meli.soccer.match_manager.common.exception.CreationConflictException;
import br.com.meli.soccer.match_manager.stadium.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StadiumValidator {

    private final StadiumRepository stadiumRepository;

    public void validateCreate(Stadium stadium) {
        validateStadiumWithSameName(stadium);
    }

    public void validateStadiumWithSameName(Stadium stadium) {
        boolean hasStadiumWithSameName = this.stadiumRepository.exists(StadiumSpecification.otherStadiumSameName(stadium.getName(), stadium.getId()));

        if(hasStadiumWithSameName) {
            throw new CreationConflictException("There is already a stadium with this name");
        }
    }
}
