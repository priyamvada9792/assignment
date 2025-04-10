package com.assignment.playerdata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "pitching_set")
public class PitchingStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;
    private String statName;
    @Column(name = "stat_value")
    private double statValue;
}
