package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.resume.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
