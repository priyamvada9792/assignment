package com.assignment.playerdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NicknameControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetNicknameEndpoint() throws Exception {
        mockMvc.perform(get("/v1/nickname?country=Japan")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Japan"))
                .andExpect(jsonPath("$.nickname").exists());
    }

    @Test
    public void testGetNicknamesFromCsvEndpoint() throws Exception {
        mockMvc.perform(get("/v1/nickname/from-csv")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].player").exists())
                .andExpect(jsonPath("$[0].country").exists())
                .andExpect(jsonPath("$[0].nickname").exists());
    }
}