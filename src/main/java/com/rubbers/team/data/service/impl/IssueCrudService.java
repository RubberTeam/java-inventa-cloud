package com.rubbers.team.data.service.impl;

import com.rubbers.team.data.entity.issue.Issue;
import com.rubbers.team.data.service.IssueRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.vaadin.artur.helpers.CrudService;

import java.util.UUID;

@SpringComponent
@AllArgsConstructor
public class IssueCrudService extends CrudService<Issue, UUID> {

    @Getter
    private IssueRepository repository;

}
