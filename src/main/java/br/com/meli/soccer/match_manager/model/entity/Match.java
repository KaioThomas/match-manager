package br.com.meli.soccer.match_manager.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "football_match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "home_club_id", referencedColumnName = "id")
    private Club homeClub;

    @ManyToOne
    @JoinColumn(name = "visiting_club_id", referencedColumnName = "id")
    private Club visitingClub;

    private int homeClubGoals;

    private int visitingClubGoals;

    @ManyToOne
    @JoinColumn(name = "stadium_id", referencedColumnName = "id")
    private Stadium stadium;

    @Column(name = "date_and_hour")
    private LocalDateTime dateAndHour;
}
