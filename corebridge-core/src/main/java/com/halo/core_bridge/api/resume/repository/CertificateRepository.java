package com.halo.core_bridge.api.resume.repository;

import com.halo.core_bridge.api.resume.model.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
