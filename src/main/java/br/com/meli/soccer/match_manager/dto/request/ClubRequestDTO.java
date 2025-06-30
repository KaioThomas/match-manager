package br.com.meli.soccer.match_manager.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClubRequestDTO {

    @NotEmpty
    @Size(min = 2)
    private String name;

    @NotEmpty
    private String acronymState;

    @NotNull
    @PastOrPresent
    private LocalDate creationDate;

    @NotNull
    private Boolean active;

    private Long id;
}
