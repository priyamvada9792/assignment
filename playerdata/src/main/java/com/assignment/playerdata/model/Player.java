package com.assignment.playerdata.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
@Setter
@Getter
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dob;

    @ManyToOne
    private Manager manager;

    @ManyToOne
    private Team team;

    // Getters and Setters
    public int getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }
}


