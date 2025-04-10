package com.assignment.playerdata;


import com.assignment.playerdata.service.NicknameGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NicknameGeneratorServiceTests {

    @Autowired
    private NicknameGeneratorService service;

    @Test
    public void testGenerateNickname() {
        Map<String, String> result = service.generateNickname("India");
        assertEquals("India", result.get("country"));
        assertTrue(result.containsKey("nickname") || result.containsKey("error"));
    }

    @Test
    public void testGenerateNicknamesFromCsv() {
        List<Map<String, String>> results = service.generateNicknamesFromCsv();
        assertFalse(results.isEmpty());
        for (Map<String, String> entry : results) {
            assertTrue(entry.containsKey("player"));
            assertTrue(entry.containsKey("country"));
            assertTrue(entry.containsKey("nickname") || entry.containsKey("error"));
        }
    }
}
