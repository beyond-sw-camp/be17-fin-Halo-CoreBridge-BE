package com.halo.core_bridge.api.organization.service;

import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.organization.repository.DepartmentRepository;
import com.halo.core_bridge.common.exception.BaseException;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    //부서 조회
    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> BaseException.from(BaseResponseStatus.DEPARTMENT_NOT_FOUND));
    }

    //JPA getReference() 활용 - 실제 쿼리 없이 프록시 반환
    public Department getReference(Long id) {
        try {
            return departmentRepository.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw BaseException.from(BaseResponseStatus.DEPARTMENT_NOT_FOUND);
        }
    }
}
