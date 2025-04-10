package com.assignment.playerdata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "batting_stat")
public class BattingStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player player;
    private String statName;
    @Column(name = "stat_value")
    private double statValue;
}

