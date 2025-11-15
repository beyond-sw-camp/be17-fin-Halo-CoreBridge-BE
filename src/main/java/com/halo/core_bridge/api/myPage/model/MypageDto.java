package com.halo.core_bridge.api.myPage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.util.List;

public class MypageDto {

    @Builder
    public record MyPageResponse(
            ProfileResponse profile,
            List<AppliedJobResponse> applications
    ) {}

    @Builder
    public record ProfileResponse(
            String name,
            String email,
            long appliedCount
    ) {}

    @Builder
    public record AppliedJobResponse(
      Long jobPostingId,
      String jobTitle,
      String departmentName,

      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
      String appliedDate,
      String currentStage,
      List<String> process
    ){}
}
