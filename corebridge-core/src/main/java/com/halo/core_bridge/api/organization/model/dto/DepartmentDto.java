package com.halo.core_bridge.api.organization.model.dto;

import com.halo.core_bridge.api.organization.model.entity.Department;
import lombok.Builder;
import lombok.Getter;


public class DepartmentDto {


    @Getter
    @Builder
    public static class Read {
        private Long id;
        private String name;

        public static Read from(Department entity) {
            return Read.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .build();
        }
    }
 }
