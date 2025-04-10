package com.assignment.playerdata;
import com.assignment.playerdata.dto.PlayerDTO;
import com.assignment.playerdata.exception.PlayerNotFoundException;
import com.assignment.playerdata.model.Player;
import com.assignment.playerdata.repository.PlayerRepository;
import com.assignment.playerdata.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceTests {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPlayers_Admin() {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("John");
        player.setLastName("Doe");
        player.setDob(LocalDate.of(2000, 1, 1));

        when(playerRepository.findAll()).thenReturn(List.of(player));

        List<PlayerDTO> players = playerService.getAllPlayers(true);
        assertEquals(1, players.size());
        assertEquals("John", players.get(0).getFirstName());
        assertEquals("Doe", players.get(0).getLastName());
        assertTrue(players.get(0).getAge() > 0);
    }

    @Test
    public void testGetAllPlayers_Regular() {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("Jane");
        player.setLastName("Smith");
        player.setDob(LocalDate.of(1995, 5, 20));

        when(playerRepository.findAll()).thenReturn(List.of(player));

        List<PlayerDTO> players = playerService.getAllPlayers(false);
        assertEquals(1, players.size());
        assertEquals("Jane", players.get(0).getFirstName());
        assertNull(players.get(0).getLastName()); // last name should be hidden for non-admin
        assertTrue(players.get(0).getAge() > 0);
    }

    @Test
    public void testGetPlayerById_Admin() {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("Alice");
        player.setLastName("Wonder");
        player.setDob(LocalDate.of(1990, 3, 15));

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        PlayerDTO result = playerService.getPlayerById(1L, true);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Wonder", result.getLastName());
    }

    @Test
    public void testGetPlayerById_NotFound() {
        when(playerRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerById(100L, false));
    }
}

