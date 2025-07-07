package br.com.meli.soccer.match_manager.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "football_match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "home_club_id", referencedColumnName = "id")
    private Club homeClub;

    @ManyToOne
    @JoinColumn(name = "visiting_club_id", referencedColumnName = "id")
    private Club visitingClub;

    private Integer homeClubGoals;

    private Integer visitingClubGoals;

    @ManyToOne
    @JoinColumn(name = "stadium_id", referencedColumnName = "id")
    private Stadium stadium;

    private LocalDateTime dateTime;
}
