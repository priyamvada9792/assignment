package com.assignment.playerdata.repository;

import com.assignment.playerdata.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {}