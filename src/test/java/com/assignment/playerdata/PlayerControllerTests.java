package com.assignment.playerdata;

import com.assignment.playerdata.model.Player;
import com.assignment.playerdata.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerService playerService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PlayerService playerService() {
            return Mockito.mock(PlayerService.class);
        }
    }

    @Test
    public void testGetAllPlayers_Admin() throws Exception {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("John");
        player.setLastName("Doe");
        player.setDob(LocalDate.of(2000, 1, 1));

        Mockito.when(playerService.getAllPlayers()).thenReturn(List.of(player));

        mockMvc.perform(get("/v1/players?isAdmin=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].age", is(notNullValue())));
    }
    @Test
    public void testGetPlayerById_Regular() throws Exception {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("John");
        player.setLastName("Doe");
        player.setDob(LocalDate.of(2000, 1, 1));

        Mockito.when(playerService.getPlayerById(1L)).thenReturn(Optional.of(player));

        mockMvc.perform(get("/v1/players/1?isAdmin=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is("John")))
                .andExpect(jsonPath("lastName").doesNotExist())
                .andExpect(jsonPath("age", is(notNullValue())));
    }

    @Test
    public void integrationTestGetPlayerById_NotFound() throws Exception {
        mockMvc.perform(get("/v1/players/9999?isAdmin=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("not found")));

    }
}