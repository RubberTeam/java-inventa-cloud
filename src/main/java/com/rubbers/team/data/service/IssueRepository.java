package com.rubbers.team.data.service;

import com.rubbers.team.data.entity.issue.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {
}
