package br.com.meli.soccer.match_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClubDTO {

    private String name;

    private String acronymState;

    private LocalDateTime creationDate = LocalDateTime.now();

    private boolean active;
}
