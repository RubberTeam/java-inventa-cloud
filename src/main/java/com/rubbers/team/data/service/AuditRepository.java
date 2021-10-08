package com.rubbers.team.data.service;

import com.rubbers.team.data.entity.audit.Audit;
import com.rubbers.team.data.entity.issue.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditRepository extends JpaRepository<Audit, UUID> {
}
