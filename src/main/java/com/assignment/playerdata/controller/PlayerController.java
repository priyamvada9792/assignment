package com.assignment.playerdata.controller;

import com.assignment.playerdata.exception.PlayerNotFoundException;
import com.assignment.playerdata.model.Player;
import com.assignment.playerdata.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {
    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    @Autowired
    private PlayerService playerService;

    @GetMapping
    public List<Map<String, Object>> getPlayers(@RequestParam boolean isAdmin) {

        return playerService.getAllPlayers().stream().map(player -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", player.getId());
            data.put("firstName", player.getFirstName());
            if (isAdmin) data.put("lastName", player.getLastName());
            data.put("age", player.getAge());
            return data;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Map<String, Object> getPlayerById(@PathVariable Long id, @RequestParam boolean isAdmin) {
        logger.info("Fetching player with ID {} as {}", id, isAdmin ? "ADMIN" : "USER");
        Player player = playerService.getPlayerById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        Map<String, Object> data = new HashMap<>();
        data.put("id", player.getId());
        data.put("firstName", player.getFirstName());
        if (isAdmin) data.put("lastName", player.getLastName());
        data.put("age", player.getAge());
        logger.debug("Returning player data: {}", data);
        return data;
    }
}

