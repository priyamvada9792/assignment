package com.assignment.playerdata;
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
    public void testGetAllPlayers() {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("John");
        player.setLastName("Doe");
        player.setDob(LocalDate.of(2000, 1, 1));

        when(playerRepository.findAll()).thenReturn(List.of(player));

        List<Player> players = playerService.getAllPlayers();
        assertEquals(1, players.size());
        assertEquals("John", players.get(0).getFirstName());
    }

    @Test
    public void testGetPlayerById() {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("Jane");
        player.setLastName("Smith");
        player.setDob(LocalDate.of(1995, 5, 20));

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        Optional<Player> result = playerService.getPlayerById(1L);
        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getFirstName());
    }

    @Test
    public void testGetPlayerById_NotFound() {
        when(playerRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<Player> result = playerService.getPlayerById(100L);
        assertFalse(result.isPresent());
    }
}
