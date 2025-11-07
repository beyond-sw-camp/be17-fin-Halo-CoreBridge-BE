package com.halo.core_bridge.api.interview.controller;

import com.halo.core_bridge.api.interview.model.dto.InterviewRoomDto;
import com.halo.core_bridge.api.interview.service.InterviewRoomService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview/rooms")
public class InterviewRoomController {

    private final InterviewRoomService interviewRoomService;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createRoom(@RequestBody InterviewRoomDto.Create create) {

        interviewRoomService.save(create);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success("면접 장소 추가 성공"));
    }

    @GetMapping("/{interviewRoomId}")
    public ResponseEntity<BaseResponse<InterviewRoomDto.Read>> getInterviewRoom(@PathVariable Long interviewRoomId) {

        InterviewRoomDto.Read findRoom = interviewRoomService.findById(interviewRoomId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(findRoom));
    }
}
