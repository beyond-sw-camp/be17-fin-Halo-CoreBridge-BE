package com.halo.core_bridge.api.ai.service;

import com.halo.core_bridge.api.ai.dto.AiDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {
    private final WebClient aiWebClient;

    public String summarize(String text) {
        return aiWebClient.post().uri("/summary")
                .bodyValue(new AiDtos.TextInput(text))
                .retrieve()
                .bodyToMono(AiDtos.SummaryRes.class)
                .map(AiDtos.SummaryRes::summary)
                .block();
    }

    public List<String> extractSkills(String text) {
        return aiWebClient.post().uri("/skills")
                .bodyValue(new AiDtos.TextInput(text))
                .retrieve()
                .bodyToMono(AiDtos.SkillsRes.class)
                .map(AiDtos.SkillsRes::skills)
                .block();
    }

    public void saveResume(String candidateId, String resumeText) {
        aiWebClient.post().uri("/save_resume")
                .bodyValue(new AiDtos.ResumeSaveReq(candidateId, resumeText))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public AiDtos.MatchRes matchJd(String jdText, List<String> requiredSkills, int topK) {
        return aiWebClient.post().uri("/match_jd")
                .bodyValue(new AiDtos.MatchRequest(jdText, requiredSkills, topK))
                .retrieve()
                .bodyToMono(AiDtos.MatchRes.class)
                .block();
    }

    public AiDtos.ScoreRes score(String jdText, String candidateId, List<String> requiredSkills) {
        return aiWebClient.post().uri("/score")
                .bodyValue(new AiDtos.ScoreRequest(jdText, candidateId, requiredSkills))
                .retrieve()
                .bodyToMono(AiDtos.ScoreRes.class)
                .block();
    }
}
