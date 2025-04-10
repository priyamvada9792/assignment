package com.assignment.playerdata;

import com.assignment.playerdata.dto.PlayerDTO;
import com.assignment.playerdata.exception.PlayerNotFoundException;
import com.assignment.playerdata.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private PlayerService playerService;

    @Test
    public void testGetAllPlayers_Admin() throws Exception {
        PlayerDTO player = new PlayerDTO();
        player.setId(1L);
        player.setFirstName("John");
        player.setLastName("Doe");
        player.setAge(24);

        Mockito.when(playerService.getAllPlayers(true)).thenReturn(List.of(player));

        mockMvc.perform(get("/v1/players?isAdmin=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].age", is(24)));
    }

    @Test
    public void testGetPlayerById_Regular() throws Exception {
        PlayerDTO player = new PlayerDTO();
        player.setId(1L);
        player.setFirstName("John");
        player.setAge(24);

        Mockito.when(playerService.getPlayerById(1L, false)).thenReturn(player);

        mockMvc.perform(get("/v1/players/1?isAdmin=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is("John")))
                .andExpect(jsonPath("lastName").doesNotExist())
                .andExpect(jsonPath("age", is(24)));
    }

    @Test
    public void integrationTestGetPlayerById_NotFound() throws Exception {
        Mockito.when(playerService.getPlayerById(9999L, true)).thenThrow(new PlayerNotFoundException(9999L));

        mockMvc.perform(get("/v1/players/9999?isAdmin=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("not found")));
    }

    @Test
    void testInvalidPlayerIdFormat() throws Exception {
        mockMvc.perform(get("/v1/players/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid input for parameter 'id'. Expected type: Long"));
    }

    @Test
    void testNonExistentPlayerId() throws Exception {
        Mockito.when(playerService.getPlayerById(99999L, false)).thenThrow(new PlayerNotFoundException(99999L));

        mockMvc.perform(get("/v1/players/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testNoPlayersPresent() throws Exception {
        Mockito.when(playerService.getAllPlayers(true)).thenReturn(List.of());

        mockMvc.perform(get("/v1/players?isAdmin=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
