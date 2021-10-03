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
package com.rubbers.team.views.item;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.rubbers.team.data.entity.Item;
import com.rubbers.team.data.entity.ItemStatus;
import com.rubbers.team.data.service.impl.ItemCrudService;
import com.rubbers.team.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PermitAll
@PageTitle("Item")
@Route(value = "item", layout = MainLayout.class)
@RouteAlias(value = "item", layout = MainLayout.class)
public class ItemView extends Div {
    private final ItemCrudService itemCrudService;

    private Div createPage;
    private Div editPage;

    public ItemView(@Autowired final ItemCrudService itemCrudService) {
        this.itemCrudService = itemCrudService;
        addClassName("item-view");
        setSizeFull();
        addTabs();
        configureCreatePage();
        configureEditPage();
    }

    private void addTabs() {
        final Tab createItemTab = new Tab("Create");
        createPage = new Div();

        final Tab editItemTab = new Tab("Edit");
        editPage = new Div();
        editPage.setVisible(false);
        editPage.setText("Edit");

        final Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(createItemTab, createPage);
        tabsToPages.put(editItemTab, editPage);

        final Tabs tabs = new Tabs(createItemTab, editItemTab);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.setFlexGrowForEnclosedTabs(1);
        final Div pages = new Div(createPage, editPage);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            final Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, pages);
    }

    private void configureCreatePage() {
        final Binder<Item> binder = new Binder<>(Item.class);

        // private UUID id = UUID.randomUUID();
        final TextField uuidField = new TextField("id");
        uuidField.setValue(UUID.randomUUID().toString());
        uuidField.setEnabled(false);
        binder.forField(uuidField).bind(x -> x.getId().toString(), (x, y) -> x.setId(UUID.fromString(y)));

        // private String serialNumber;
        final TextField serialField = new TextField("Serial number");
        serialField.setPlaceholder("Write here serial number or specific identifier");
        serialField.setClearButtonVisible(true);
        binder.forField(serialField).bind(Item::getSerialNumber, Item::setSerialNumber);

        // private String description;
        final TextArea descriptionArea = new TextArea("Description");
        descriptionArea.setPlaceholder("Write here something about valuable object");
        descriptionArea.setClearButtonVisible(true);
        binder.forField(descriptionArea)
                .withValidator(x -> !StringUtils.isBlank(x), "Description should not be empty")
                .bind(Item::getDescription, Item::setDescription);

        // private LocalDate lastUpdate;
        final DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        binder.forField(datePicker).bind(Item::getLastUpdate, Item::setLastUpdate);

        // private String lastTask;
        final TextField taskField = new TextField("Task identifier");
        taskField.setPlaceholder("Write here id of the task which the item should have been inventoried");
        taskField.setClearButtonVisible(true);
        binder.forField(taskField).bind(Item::getLastTask, Item::setLastTask);

        // private int count = 1;
        final IntegerField countField = new IntegerField("Items count");
        countField.setValue(1);
        countField.setMin(1);
        countField.setHasControls(true);
        binder.forField(countField).bind(Item::getCount, Item::setCount);

        // private String status;
        // final ListBox<String> statusBox = new ListBox<>();
        // statusBox.setItems(Stream.of(ItemStatus.values()).map(ItemStatus::getStatusName));
        final Select<String> statusBox = new Select<>("Status");
        statusBox.setItems(Stream.of(ItemStatus.values()).map(ItemStatus::getStatusName));
        statusBox.setValue(ItemStatus.OK.getStatusName());
        binder.forField(statusBox).bind(Item::getStatus, Item::setStatus);

        // private String location;
        final TextField locationField = new TextField("Object location");
        locationField.setPlaceholder("Write here object address and describe how to find it");
        locationField.setClearButtonVisible(true);
        binder.forField(locationField).bind(Item::getLocation, Item::setLocation);

        // private String issue;
        final TextField issueField = new TextField("Issue");
        issueField.setPlaceholder("Write here id of issue or describe what happened with object");
        issueField.setClearButtonVisible(true);
        binder.forField(issueField).bind(Item::getIssue, Item::setIssue);

        final Button createButton = new Button("Create", buttonClickEvent -> {
            final Item clearItem = Item.builder().build();
            try {
                binder.writeBean(clearItem);
                itemCrudService.getRepository().save(clearItem);
                final Notification notification = new Notification(
                        "Added new item with id " + clearItem.getId(),
                        3000, Notification.Position.BOTTOM_END);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.open();
                uuidField.setValue(UUID.randomUUID().toString());
            } catch (ValidationException validationException) {
                final Notification notification = new Notification(
                        "Validation data error: " + validationException.getMessage(),
                        3000, Notification.Position.BOTTOM_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            } catch (Exception e) {
                log.error("Unable save new item into db", e);
                e.printStackTrace();
                final Notification notification = new Notification(
                        "Error has occurred: " + e.getMessage() + " . Please contact IT-administrator",
                        3000, Notification.Position.BOTTOM_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        });

        final VerticalLayout layout = new VerticalLayout();
        layout.setFlexGrow(1);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.add(
                uuidField,
                serialField,
                descriptionArea,
                datePicker,
                taskField,
                countField,
                statusBox,
                locationField,
                issueField,
                createButton);
        createPage.add(layout);
    }

    private void configureEditPage() {}
}