package com.assignment.playerdata.controller;

import com.assignment.playerdata.dto.PlayerDTO;
import com.assignment.playerdata.exception.PlayerNotFoundException;
import com.assignment.playerdata.model.Player;
import com.assignment.playerdata.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(@RequestParam(defaultValue = "false") boolean isAdmin) {
        return ResponseEntity.ok(playerService.getAllPlayers(isAdmin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id,
                                                   @RequestParam(defaultValue = "false") boolean isAdmin) {
        return ResponseEntity.ok(playerService.getPlayerById(id, isAdmin));
    }
}

