/*
 * Copyright (c) 2021 Simeshin AM <simeshin.a.m@sberbank.ru>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.rubbers.team.views.audit;

import com.rubbers.team.data.entity.audit.Audit;
import com.rubbers.team.data.service.impl.AuditCrudService;
import com.rubbers.team.views.MainLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

/**
 * Вьюха для событий аудита
 */
@Slf4j
@PermitAll
@PageTitle("Аудит")
@Route(value = "audit-list", layout = MainLayout.class)
public class AuditView extends Div {
    private final AuditCrudService auditCrudService;

    private Grid<Audit> grid;

    public AuditView(@Autowired final AuditCrudService auditCrudService) {
        this.auditCrudService = auditCrudService;
        addClassName("audit-list-view");
        setSizeFull();
        createGrid();
        add(grid);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
    }

    private void createGridComponent() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");
        grid.setItems(auditCrudService.getRepository().findAll());
    }

    private void addColumnsToGrid() {
        grid.addColumn(Audit::getAuditEvenTime)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setResizable(true)
                .setHeader("Время события")
                .setAutoWidth(true);
        grid.addColumn(Audit::getAuditDescription)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setResizable(true)
                .setHeader("Описание события")
                .setAutoWidth(true);
    }
}
