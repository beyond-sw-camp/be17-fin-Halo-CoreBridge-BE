package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.dto.InterviewRoomDto;
import com.halo.core_bridge.api.interview.model.entity.Room;
import com.halo.core_bridge.api.interview.model.enums.InterviewType;
import com.halo.core_bridge.api.interview.repository.InterviewRoomRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewRoomService {

    private final InterviewRoomRepository interviewRoomRepository;

    /**
     * 면접 장소를 추가한다.
     * @param create 면접 장소 데이터를 저장하고 있는 DTO
     * @return 새롭게 추가된 면접 장소의 식별자 <code>id</code>
     * @throws BaseException 위치가 중복일 경우 예외 처리, 그외 JPA의 예외처리를 받아 BaseException으로 변환
     */
    @Transactional
    public Long save(InterviewRoomDto.Create create) {

        try {

            // 장소 중복 체크
            if (interviewRoomRepository.existsRoomByLocation(create.getLocation())) {

                log.error("[ERROR] {}", BaseResponseStatus.DUPLICATE_INTERVIEW_ROOM);
                throw BaseException.from(BaseResponseStatus.DUPLICATE_INTERVIEW_ROOM);

            }

            Room savedRoom = interviewRoomRepository.save(create.toEntity());
            return savedRoom.getId();

        } catch (DataIntegrityViolationException e) {

            log.error(e.getMessage());
            throw BaseException.from(BaseResponseStatus.GLOBAL_EXCEPTION);
        }
    }

    /**
     * 면접 장소 상세 조회
     * @param interviewRoomId 조회하기 위한 면접 장소 <code>id</code>
     * @return <code>InterviewRoomDto.Read</code>
     * @throws BaseException <code>id</code>에 맞는 데이터가 없는 경우 예외 발생
     */
    public InterviewRoomDto.Read findById(Long interviewRoomId) {

        Room findRoom = interviewRoomRepository.findById(interviewRoomId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_INTERVIEW_ROOM));

        return InterviewRoomDto.Read.fromEntity(findRoom);
    }

    /**
     * 면접 장소 목록 조회
     * @return <code>InterviewRoomDto.InterviewRoomList</code> 면접 장소의 목록을 저장한 DTO
     */
    public InterviewRoomDto.InterviewRoomList findAll() {

        List<Room> rooms = interviewRoomRepository.findAll();
        return InterviewRoomDto.InterviewRoomList.fromEntity(rooms);
    }

    /**
     * 면접 장소 삭제
     * @param interviewRoomId 삭제 하려는 면접 장소의 <code>id</code>
     */
    public void deleteById(Long interviewRoomId) {

        // 삭제하려는 장소에 면접일정이 등록되어있는지 확인하는 로직 추후 추가 예정

        interviewRoomRepository.deleteById(interviewRoomId);
    }


    /**
     * 면접 장소를 수정합니다.
     * @param roomId - 수정할 면접 장소의 <code>id</code>
     * @param update - 수정데이터
     * @throws BaseException 존재하지 않는 면접일 경우 예외 발생
     */
    @Transactional
    public void update(Long roomId, InterviewRoomDto.Update update) {

        Room findRoom = interviewRoomRepository.findById(roomId)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.NOT_FOUND_INTERVIEW_ROOM));

        if (update.getLocation() != null)
            findRoom.changeLocation(update.getLocation());

        if (update.getName() != null)
            findRoom.changeName(update.getName());

        InterviewType updatedInterviewType = update.getInterviewType();

        if (updatedInterviewType != null) {

            if (updatedInterviewType == InterviewType.ONLINE) {

                findRoom.changeCapacity(null); // 온라인룸은 수용인원 없음

            } else if (updatedInterviewType == InterviewType.OFFLINE && update.getCapacity() == null) {

                throw BaseException.from(BaseResponseStatus.NOT_PROVIDED_CAPACITY_FOR_OFFLINE_ROOM); // 오프라인룸은 수용인원 필수

            }

            findRoom.changeRoomType(updatedInterviewType);
        }

        if (update.getCapacity() != null)
            findRoom.changeCapacity(update.getCapacity());

        if (update.getInterviewType() != null)
            findRoom.changeRoomType(update.getInterviewType());

        if (update.getDescription() != null)
            findRoom.changeDescription(update.getDescription());
    }
}
