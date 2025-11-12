package com.halo.core_bridge.api.management.repository;

import com.halo.core_bridge.api.management.model.dto.ManagementDto;

public interface ManagementQueryRepository {
    ManagementDto findManagementByJobPostingId(Long jobPostingId);
}
