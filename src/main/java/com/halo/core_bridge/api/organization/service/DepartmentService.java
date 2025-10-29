package com.halo.core_bridge.api.organization.service;

import com.halo.core_bridge.api.organization.model.dto.DepartmentDto;
import com.halo.core_bridge.api.organization.model.entity.Department;
import com.halo.core_bridge.api.organization.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    //부서 조회
//    public Department getById(Long id) {
//        return departmentRepository.findById(id)
//                .orElseThrow(() -> BaseException.from(BaseResponseStatus.DEPARTMENT_NOT_FOUND));
//    }

    //전체 조회
    public List<DepartmentDto.Read> getAllDepartments() {
        List<DepartmentDto.Read> departmentDtoList = new ArrayList<>();
        List<Department> departmentList = departmentRepository.findAll();
        for (Department department : departmentList) {
            departmentDtoList.add(DepartmentDto.Read.from(department));
        }
        return departmentDtoList;
    }

}
