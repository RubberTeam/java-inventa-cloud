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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.security.PermitAll;

import org.apache.commons.lang3.StringUtils;

import com.rubbers.team.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import lombok.val;

@PermitAll
@PageTitle("Items")
@Route(value = "items", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ListView extends Div {

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
    private Grid.Column<Item> mapLocationColumn;
    private Grid.Column<Item> issueColumn;

    public ListView() {
        addClassName("list-view");
        setSizeFull();
        createGrid();
        add(grid);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new Grid<>();
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        final List<Item> items = getClients();
        gridListDataView = grid.setItems(items);
    }

    private void addColumnsToGrid() {
        idColumn = grid.addColumn(Item::getId)
                .setResizable(true)
                .setHeader("id")
                .setAutoWidth(true);
        descriptionColumn = grid.addColumn(Item::getDescription)
                .setResizable(true)
                .setHeader("description")
                .setAutoWidth(true);
        countColumn = grid.addColumn(Item::getCount)
                .setComparator(Item::getCount)
                .setResizable(true)
                .setHeader("count")
                .setAutoWidth(true);
        statusColumn = grid.addColumn(Item::getStatus)
                .setComparator(Item::getStatus)
                .setResizable(true)
                .setHeader("status")
                .setAutoWidth(true);
        serialNumberColumn = grid.addColumn(Item::getSerialNumber)
                .setResizable(true)
                .setHeader("serial")
                .setAutoWidth(true);
        lastUpdateColumn = grid.addColumn(
                        new LocalDateRenderer<>(Item::getLastUpdate, DateTimeFormatter.ofPattern("d/M/yyyy")))
                .setComparator(Item::getLastUpdate)
                .setResizable(true)
                .setHeader("updated")
                .setAutoWidth(true);
        lastTaskColumn = grid.addColumn(Item::getLastTask)
                .setResizable(true)
                .setHeader("task")
                .setAutoWidth(true);
        locationColumn = grid.addColumn(Item::getLocation)
                .setResizable(true)
                .setHeader("location")
                .setAutoWidth(true);
        mapLocationColumn = grid.addColumn(Item::getMapLocation)
                .setResizable(true)
                .setHeader("map")
                .setAutoWidth(true);
        issueColumn = grid.addColumn(Item::getIssue)
                .setResizable(true)
                .setHeader("issue")
                .setAutoWidth(true);
    }

    // private void createIdColumn() {
    // idColumn = grid.addColumn(Item::getId, "id").setHeader("ID").setWidth("120px").setFlexGrow(0);
    // }

    // private void createClientColumn() {
    // clientColumn = grid.addColumn(new ComponentRenderer<>(client -> {
    // HorizontalLayout hl = new HorizontalLayout();
    // hl.setAlignItems(Alignment.CENTER);
    // Image img = new Image(client.getImg(), "");
    // Span span = new Span();
    // span.setClassName("name");
    // span.setText(client.getClient());
    // hl.add(img, span);
    // return hl;
    // })).setComparator(client -> client.getClient()).setHeader("Client");
    // }
    //
    // private void createAmountColumn() {
    // amountColumn = grid
    // .addColumn(Item::getAmount,
    // new NumberRenderer<>(client -> client.getAmount(), NumberFormat.getCurrencyInstance(Locale.US)))
    // .text((item, newValue) -> item.setAmount(Double.parseDouble(newValue)))
    // .setComparator(client -> client.getAmount()).setHeader("Amount");
    // }
    //
    // private void createStatusColumn() {
    // statusColumn = grid.addEditColumn(Item::getClient, new ComponentRenderer<>(client -> {
    // Span span = new Span();
    // span.setText(client.getStatus());
    // span.getElement().setAttribute("theme", "badge " + client.getStatus().toLowerCase());
    // return span;
    // })).select((item, newValue) -> item.setStatus(newValue), Arrays.asList("Pending", "Success", "Error"))
    // .setComparator(client -> client.getStatus()).setHeader("Status");
    // }
    //
    // private void createDateColumn() {
    // dateColumn = grid
    // .addColumn(new LocalDateRenderer<>(client -> LocalDate.parse(client.getDate()),
    // DateTimeFormatter.ofPattern("M/d/yyyy")))
    // .setComparator(client -> client.getDate()).setHeader("Date").setWidth("180px").setFlexGrow(0);
    // }
    //

    private void addFiltersToGrid() {
        final HeaderRow filterRow = grid.appendHeaderRow();

        final TextField idFilter = new TextField();
        idFilter.setPlaceholder("Filter");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getId().toString(), idFilter.getValue())));
        filterRow.getCell(idColumn).setComponent(idFilter);

        final TextField descriptionFilter = new TextField();
        descriptionFilter.setPlaceholder("Filter");
        descriptionFilter.setClearButtonVisible(true);
        descriptionFilter.setWidth("100%");
        descriptionFilter.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getDescription(), descriptionFilter.getValue())
                )
        );
        filterRow.getCell(descriptionColumn).setComponent(descriptionFilter);

        final ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Arrays.asList("ok", "out of service", "issue", "missed"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> areStatusesEqual(item, statusFilter)
                )
        );
        filterRow.getCell(statusColumn).setComponent(statusFilter);

        final TextField countFilter = new TextField();
        countFilter.setPlaceholder("Filter");
        countFilter.setClearButtonVisible(true);
        countFilter.setWidth("100%");
        countFilter.setValueChangeMode(ValueChangeMode.EAGER);
        countFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> item.getCount() == Integer.parseInt(countFilter.getValue())
                )
        );
        filterRow.getCell(countColumn).setComponent(countFilter);

        final TextField serialFilter = new TextField();
        serialFilter.setPlaceholder("Filter");
        serialFilter.setClearButtonVisible(true);
        serialFilter.setWidth("100%");
        serialFilter.setValueChangeMode(ValueChangeMode.EAGER);
        serialFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> StringUtils.containsIgnoreCase(item.getSerialNumber(), serialFilter.getValue())
                )
        );
        filterRow.getCell(serialNumberColumn).setComponent(serialFilter);

        final DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Before");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(
                        item -> areDateAfter(item, dateFilter)
                )
        );
        filterRow.getCell(lastUpdateColumn).setComponent(dateFilter);

        // TextField clientFilter = new TextField();
        // clientFilter.setPlaceholder("Filter");
        // clientFilter.setClearButtonVisible(true);
        // clientFilter.setWidth("100%");
        // clientFilter.setValueChangeMode(ValueChangeMode.EAGER);
        // clientFilter.addValueChangeListener(event -> gridListDataView
        // .addFilter(client -> StringUtils.containsIgnoreCase(client.getClient(), clientFilter.getValue())));
        // filterRow.getCell(clientColumn).setComponent(clientFilter);

        // TextField amountFilter = new TextField();
        // amountFilter.setPlaceholder("Filter");
        // amountFilter.setClearButtonVisible(true);
        // amountFilter.setWidth("100%");
        // amountFilter.setValueChangeMode(ValueChangeMode.EAGER);
        //// amountFilter.addValueChangeListener(event -> gridListDataView.addFilter(client -> StringUtils
        //// .containsIgnoreCase(Double.toString(client.getAmount()), amountFilter.getValue())));
        // filterRow.getCell(amountColumn).setComponent(amountFilter);

        // ComboBox<String> statusFilter = new ComboBox<>();
        // statusFilter.setItems(Arrays.asList("Pending", "Success", "Error"));
        // statusFilter.setPlaceholder("Filter");
        // statusFilter.setClearButtonVisible(true);
        // statusFilter.setWidth("100%");
        //// statusFilter.addValueChangeListener(
        //// event -> gridListDataView.addFilter(client -> areStatusesEqual(client, statusFilter)));
        // filterRow.getCell(statusColumn).setComponent(statusFilter);
    }

    private boolean areStatusesEqual(final Item client, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(client.getStatus(), statusFilterValue);
        }
        return true;
    }

    private boolean areDatesEqual(final Item item, final DatePicker dateFilter) {
        final LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            return dateFilterValue.equals(item.getLastUpdate());
        }
        return true;
    }

    private boolean areDateAfter(final Item item, final DatePicker dateFilter) {
        final LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            return dateFilterValue.isAfter(item.getLastUpdate());
        }
        return true;
    }

    private List<Item> getClients() {
        val items = new ArrayList<Item>();
        for (int i = 0; i < 100; i++) {
            items.add(Item.getRandom());
        }
        return items;
    }

}
