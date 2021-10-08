package com.rubbers.team.data.service.impl;

import com.rubbers.team.data.entity.audit.Audit;
import com.rubbers.team.data.service.AuditRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.vaadin.artur.helpers.CrudService;

import java.util.UUID;

@SpringComponent
@AllArgsConstructor
public class AuditCrudService extends CrudService<Audit, UUID> {

    @Getter
    private AuditRepository repository;
}
