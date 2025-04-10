package com.assignment.playerdata.service;

import com.assignment.playerdata.dto.PlayerDTO;
import com.assignment.playerdata.exception.PlayerNotFoundException;
import com.assignment.playerdata.model.Player;
import com.assignment.playerdata.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    @Autowired
    private PlayerRepository playerRepository;

    public List<PlayerDTO> getAllPlayers(boolean isAdmin) {
        logger.info("Fetching all players. Admin view: {}", isAdmin);
        List<PlayerDTO> players = playerRepository.findAll().stream()
                .map(player -> mapToDTO(player, isAdmin))
                .collect(Collectors.toList());
        logger.debug("Total players fetched: {}", players.size());
        return players;
    }

    public PlayerDTO getPlayerById(Long id, boolean isAdmin) {
        logger.info("Fetching player with ID {}. Admin view: {}", id, isAdmin);
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Player with ID {} not found", id);
                    return new PlayerNotFoundException(id);
                });
        PlayerDTO dto = mapToDTO(player, isAdmin);
        logger.debug("Returning player DTO: {}", dto);
        return dto;
    }

    private PlayerDTO mapToDTO(Player player, boolean isAdmin) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setFirstName(player.getFirstName());
        if (isAdmin) dto.setLastName(player.getLastName());
        dto.setAge(calculateAge(player.getDob()));
        return dto;
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}