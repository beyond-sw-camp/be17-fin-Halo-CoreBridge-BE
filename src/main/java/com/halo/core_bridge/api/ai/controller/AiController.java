package com.halo.core_bridge.api.ai.controller;

import com.halo.core_bridge.api.ai.dto.AiDtos;
import com.halo.core_bridge.api.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

    private final AiService service;

    @PostMapping("/summary")
    public ResponseEntity<AiDtos.SummaryRes> summary(@RequestBody AiDtos.TextInput req) {
        String s = service.summarize(req.text());
        return ResponseEntity.ok(new AiDtos.SummaryRes(s));
    }

    @PostMapping("/skills")
    public ResponseEntity<AiDtos.SkillsRes> skills(@RequestBody AiDtos.TextInput req) {
        return ResponseEntity.ok(new AiDtos.SkillsRes(service.extractSkills(req.text())));
    }

    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody AiDtos.ResumeSaveReq req) {
        service.saveResume(req.candidate_id(), req.resume_text());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/match")
    public ResponseEntity<AiDtos.MatchRes> match(@RequestBody AiDtos.MatchRequest req) {
        return ResponseEntity.ok(service.matchJd(req.jd_text(), req.required_skills(), req.top_k()));
    }

    @PostMapping("/score")
    public ResponseEntity<AiDtos.ScoreRes> score(@RequestBody AiDtos.ScoreRequest req) {
        return ResponseEntity.ok(service.score(req.jd_text(), req.candidate_id(), req.required_skills()));
    }
}
