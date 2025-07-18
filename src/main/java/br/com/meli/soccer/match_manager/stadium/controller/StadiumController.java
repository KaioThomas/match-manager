package br.com.meli.soccer.match_manager.stadium.controller;

import br.com.meli.soccer.match_manager.common.constants.SchemaConstants;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumCreateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.request.StadiumUpdateRequest;
import br.com.meli.soccer.match_manager.stadium.dto.response.StadiumResponse;
import br.com.meli.soccer.match_manager.stadium.service.StadiumService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stadium")
@RequiredArgsConstructor
public class StadiumController {

    private final StadiumService stadiumService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StadiumResponse create(
            @Valid
            @RequestBody
            final StadiumCreateRequest stadiumCreateRequest
    ) {
        return this.stadiumService.create(stadiumCreateRequest);
    }

    @PutMapping
    public StadiumResponse update(
            @Valid
            @RequestBody
            final StadiumUpdateRequest stadiumUpdateRequest
    ) {
        return this.stadiumService.update(stadiumUpdateRequest);
    }

    @GetMapping("/{id}")
    public StadiumResponse getById(
            @PathVariable
            @Parameter(description = SchemaConstants.STADIUM.ID_DESC, example = SchemaConstants.STADIUM.ID_EXAMPLE)
            final String id
    ) {
        return this.stadiumService.getById(id);
    }

    @GetMapping("/findAll")
    public List<StadiumResponse> getAll(
            @RequestParam(required = false)
            @Parameter(description = SchemaConstants.STADIUM.NAME_DESC, example = SchemaConstants.STADIUM.NAME_EXAMPLE)
            final String name,

            @ParameterObject
            final Pageable pageable
    ) {
        return this.stadiumService.getAll(name, pageable);
    }

}
