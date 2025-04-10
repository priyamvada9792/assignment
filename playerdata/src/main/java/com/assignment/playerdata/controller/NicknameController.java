package com.assignment.playerdata.controller;


import com.assignment.playerdata.service.NicknameGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/nickname")
public class NicknameController {

    @Autowired
    private NicknameGeneratorService nicknameGeneratorService;

    @GetMapping
    public Map<String, String> getNickname(@RequestParam String country) {
        return nicknameGeneratorService.generateNickname(country);
    }

    @GetMapping("/from-csv")
    public List<Map<String, String>> getNicknamesFromCsv() {
        return nicknameGeneratorService.generateNicknamesFromCsv();
    }
}
