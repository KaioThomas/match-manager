package br.com.meli.soccer.match_manager.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
}
