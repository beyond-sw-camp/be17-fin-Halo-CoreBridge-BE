package com.halo.core_bridge.api.interview.controller;

import com.halo.core_bridge.api.interview.model.dto.InterviewRoomDto;
import com.halo.core_bridge.api.interview.repository.InterviewRoomRepository;
import com.halo.core_bridge.api.interview.service.InterviewRoomService;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponse;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview/rooms")
public class InterviewRoomController {

    private final InterviewRoomService interviewRoomService;
    private final InterviewRoomRepository interviewRoomRepository;

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

    @GetMapping
    public ResponseEntity<BaseResponse<InterviewRoomDto.InterviewRoomList>> getInterviewRooms() {

        InterviewRoomDto.InterviewRoomList findInterviewRooms = interviewRoomService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(findInterviewRooms));
    }

    @DeleteMapping("/{interviewRoomId}")
    public ResponseEntity<BaseResponse<Object>> deleteInterviewRoom(@PathVariable Long interviewRoomId) {

        if (!interviewRoomRepository.existsById(interviewRoomId)) {
            throw BaseException.from(BaseResponseStatus.NOT_FOUND_INTERVIEW_ROOM);
        }

        interviewRoomService.deleteById(interviewRoomId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("면접 장소 삭제 완료"));
    }

    @PatchMapping("/{interviewRoomId}")
    public ResponseEntity<BaseResponse<Object>> updateInterviewRoom(@PathVariable Long interviewRoomId,
                                                                    @RequestBody InterviewRoomDto.Update update) {

        interviewRoomService.update(interviewRoomId, update);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success("수정 완료"));
    }
}
