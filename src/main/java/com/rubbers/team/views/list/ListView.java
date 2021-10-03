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
package com.rubbers.team.views.list;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.rubbers.team.data.entity.Item;
import com.rubbers.team.data.entity.ItemStatus;
import com.rubbers.team.data.service.impl.ItemCrudService;
import com.rubbers.team.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * Вьюха для описания объектов на странице сборки задания
 */
@Slf4j
@PermitAll
@PageTitle("Items")
@Route(value = "items", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ListView extends Div {
    private final ItemCrudService itemCrudService;

    private Grid<Item> grid;
    private GridListDataView<Item> gridListDataView;

    private Grid.Column<Item> idColumn;
    private Grid.Column<Item> serialNumberColumn;
    private Grid.Column<Item> descriptionColumn;
    private Grid.Column<Item> lastUpdateColumn;
    private Grid.Column<Item> lastTaskColumn;
    private Grid.Column<Item> countColumn;
    private Grid.Column<Item> statusColumn;
    private Grid.Column<Item> locationColumn;
    private Grid.Column<Item> issueColumn;

    private Set<Item> selectedCandidatesForTask;

    public ListView(@Autowired final ItemCrudService itemCrudService) {
        this.itemCrudService = itemCrudService;
        addClassName("list-view");
        setSizeFull();
        createGrid();
        add(grid);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
        // addFooterWithButtons();
        addContextItems();
    }

    private void createGridComponent() {
        grid = new Grid<>();
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");
        grid.asMultiSelect().addValueChangeListener(event -> selectedCandidatesForTask = event.getValue());

        gridListDataView = grid.setItems(itemCrudService.getRepository().findAll());
    }

    private void addColumnsToGrid() {
        idColumn = grid.addColumn(Item::getId)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setResizable(true)
                .setHeader("id")
                .setAutoWidth(true);
        descriptionColumn = grid.addColumn(Item::getDescription)
                .setResizable(true)
                .setHeader("description")
                .setAutoWidth(true);
        countColumn = grid.addColumn(Item::getCount)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setComparator(Item::getCount)
                .setResizable(true)
                .setHeader("count")
                .setAutoWidth(true);
        statusColumn = grid.addColumn(Item::getStatus)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setComparator(Item::getStatus)
                .setResizable(true)
                .setHeader("status")
                .setAutoWidth(true);
        serialNumberColumn = grid.addColumn(Item::getSerialNumber)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setResizable(true)
                .setHeader("serial")
                .setAutoWidth(true);
        lastUpdateColumn = grid.addColumn(
                new LocalDateRenderer<>(
                        Item::getLastUpdate,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .setTextAlign(ColumnTextAlign.CENTER)
                .setComparator(Item::getLastUpdate)
                .setResizable(true)
                .setHeader("updated")
                .setAutoWidth(true);
        lastTaskColumn = grid.addColumn(Item::getLastTask)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setResizable(true)
                .setHeader("task")
                .setAutoWidth(true);
        locationColumn = grid.addColumn(Item::getLocation)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setComparator(Item::getLocation)
                .setResizable(true)
                .setHeader("location")
                .setAutoWidth(true);
        issueColumn = grid.addColumn(Item::getIssue)
                .setTextAlign(ColumnTextAlign.CENTER)
                .setResizable(true)
                .setHeader("issue")
                .setAutoWidth(true);
    }

    private void addFiltersToGrid() {
        val filterRow = grid.appendHeaderRow();

        // Частичный фильтр по ID
        val idFilter = new TextField();
        idFilter.setPlaceholder("Filter");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getId().toString(), idFilter.getValue())));
        filterRow.getCell(idColumn).setComponent(idFilter);

        // Частичный фильтр по описанию
        val descriptionFilter = new TextField();
        descriptionFilter.setPlaceholder("Filter");
        descriptionFilter.setClearButtonVisible(true);
        descriptionFilter.setWidth("100%");
        descriptionFilter.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getDescription(), descriptionFilter.getValue())));
        filterRow.getCell(descriptionColumn).setComponent(descriptionFilter);

        // Точный фильтр по количеству
        val countFilter = new TextField();
        countFilter.setPlaceholder("Filter");
        countFilter.setClearButtonVisible(true);
        countFilter.setWidth("100%");
        countFilter.setValueChangeMode(ValueChangeMode.EAGER);
        countFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> item.getCount() == Integer.parseInt(countFilter.getValue())));
        filterRow.getCell(countColumn).setComponent(countFilter);

        // Точный ильтр по статусу с комбо-боксом
        val statusFilter = new ComboBox<String>();
        statusFilter.setItems(Arrays.asList("ok", "out of service", "issue", "missed"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> {
                            val statusFilterValue = statusFilter.getValue();
                            if (statusFilterValue != null) {
                                return StringUtils.equals(item.getStatus(), statusFilterValue);
                            }
                            return true;
                        }));
        filterRow.getCell(statusColumn).setComponent(statusFilter);

        // Частичный фильтр по серийному номеру
        val serialFilter = new TextField();
        serialFilter.setPlaceholder("Filter");
        serialFilter.setClearButtonVisible(true);
        serialFilter.setWidth("100%");
        serialFilter.setValueChangeMode(ValueChangeMode.EAGER);
        serialFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getSerialNumber(), serialFilter.getValue())));
        filterRow.getCell(serialNumberColumn).setComponent(serialFilter);

        // Общий фильтр по дате ДО выбранной
        val dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Before");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> {
                            val dateFilterValue = dateFilter.getValue();
                            if (dateFilterValue != null) {
                                return dateFilterValue.isAfter(item.getLastUpdate());
                            }
                            return true;
                        }));
        filterRow.getCell(lastUpdateColumn).setComponent(dateFilter);

        // Частичный фильтр по таскам
        val taskFilter = new TextField();
        taskFilter.setPlaceholder("Filter");
        taskFilter.setClearButtonVisible(true);
        taskFilter.setWidth("100%");
        taskFilter.setValueChangeMode(ValueChangeMode.EAGER);
        taskFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getLastTask(), taskFilter.getValue())));
        filterRow.getCell(lastTaskColumn).setComponent(taskFilter);

        // Частичный фильтр по локации
        val locationFilter = new TextField();
        locationFilter.setPlaceholder("Filter");
        locationFilter.setClearButtonVisible(true);
        locationFilter.setWidth("100%");
        locationFilter.setValueChangeMode(ValueChangeMode.EAGER);
        locationFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getLocation(), locationFilter.getValue())));
        filterRow.getCell(locationColumn).setComponent(locationFilter);

        // Частичный фильтр по ищью
        val issueFilter = new TextField();
        issueFilter.setPlaceholder("Filter");
        issueFilter.setClearButtonVisible(true);
        issueFilter.setWidth("100%");
        issueFilter.setValueChangeMode(ValueChangeMode.EAGER);
        issueFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getIssue(), issueFilter.getValue())));
        filterRow.getCell(issueColumn).setComponent(issueFilter);
    }

    // Оно нам нужно вообще?
    private void addFooterWithButtons() {
        val taskButton = new Button("Create task", event -> {
            log.info("Размер выбранного списка: {}", selectedCandidatesForTask.size());
            // Создать попап окно с предложением ассайна на кого-то
            // После закрытия окна создать нотификацию в любой из сторон
        });

        val footerRaw = grid.appendFooterRow();
        footerRaw.getCell(idColumn).setComponent(taskButton);
    }

    private void addContextItems() {
        val contextMenu = new GridContextMenu<>(grid);
        val editItem = contextMenu.addItem("edit item");
        val createTask = contextMenu.addItem("create task");
        val refresh = contextMenu.addItem("refresh", event -> gridListDataView.refreshAll());
    }

}