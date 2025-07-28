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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stadium")
@RequiredArgsConstructor
public class StadiumController {

    private final StadiumService stadiumService;

    @PostMapping
    public ResponseEntity<StadiumResponse> create(
            @Valid
            @RequestBody
            final StadiumCreateRequest stadiumCreateRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.stadiumService.create(stadiumCreateRequest));
    }

    @PutMapping
    public ResponseEntity<StadiumResponse> update(
            @Valid
            @RequestBody
            final StadiumUpdateRequest stadiumUpdateRequest
    ) {
        return ResponseEntity.ok(this.stadiumService.update(stadiumUpdateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumResponse> getById(
            @PathVariable
            @Parameter(description = SchemaConstants.STADIUM.ID_DESC, example = SchemaConstants.STADIUM.ID_EXAMPLE)
            final String id
    ) {
        return  ResponseEntity.ok(this.stadiumService.getById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<StadiumResponse>> getAll(
            @ParameterObject
            final Pageable pageable
    ) {
        return  ResponseEntity.ok(this.stadiumService.getAll(pageable));
    }

}
