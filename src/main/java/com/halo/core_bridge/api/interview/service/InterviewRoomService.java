package com.halo.core_bridge.api.interview.service;

import com.halo.core_bridge.api.interview.model.dto.InterviewRoomDto;
import com.halo.core_bridge.api.interview.model.entity.Room;
import com.halo.core_bridge.api.interview.repository.InterviewRoomRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
