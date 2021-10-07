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

import com.rubbers.team.data.entity.issue.Issue;
import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.entity.item.ItemCategory;
import com.rubbers.team.data.entity.item.ItemStatus;
import com.rubbers.team.data.service.impl.ItemCrudService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemForm extends FormLayout {
    Binder<Item> binder = new BeanValidationBinder<>(Item.class);
    ItemCrudService itemCrudService;
    GridListDataView<Item> gridListDataView;
    private Item item;

    // Checkbox itemIsUsed = new Checkbox("Is Used");
    TextField itemDescription = new TextField("Описание");
    TextField itemSerialNumber = new TextField("Серийный номер");
    TextField itemInventoryNumber = new TextField("Инвентарный номер");
    TextField itemCode = new TextField("Табельный номер");
    TextField itemBatchNumber = new TextField("Номер партии");
    IntegerField itemCount = new IntegerField("Количество предметов");
    IntegerField itemPrice = new IntegerField("Стоимость");
    TextField itemCurrencyType = new TextField("Код валюты");
    TextField itemFactoryName = new TextField("Название завода");
    TextField itemWareHouseName = new TextField("Название склада");
    TextField itemDivisionName = new TextField("Подразделение");
    TextField itemLocation = new TextField("Адрес");
    TextField itemOwner = new TextField("МОЛ");
    TextField taskID = new TextField("Последняя задача");
    DatePicker itemLastUpdate = new DatePicker("Дата последнего обновления");
    ComboBox<ItemCategory> itemCategory = new ComboBox<>("Категория ценности");
    ComboBox<ItemStatus> statusBox = new ComboBox<>("Статус");
    ComboBox<Issue> itemIssue = new ComboBox<>("Проблема");
    Button save;
    Button close;


    public ItemForm(ItemCrudService itemCrudService, GridListDataView<Item> gridListDataView) {
        addClassName("item-form");

        this.itemCrudService = itemCrudService;
        this.gridListDataView = gridListDataView;

        // binder.bindInstanceFields(this);
        statusBox.setItems(ItemStatus.values());
        statusBox.setValue(ItemStatus.ON_BUY);
        binder.forField(statusBox).bind(Item::getItemStatus, Item::setItemStatus);

        itemCategory.setItems(ItemCategory.values());
        itemCategory.setValue(ItemCategory.GUN);
        binder.forField(itemCategory).bind(Item::getItemCategory, Item::setItemCategory);

        binder.forField(itemIssue);

        itemCount.setValue(1);
        itemCount.setMin(1);
        itemCount.setHasControls(true);
        binder.forField(itemCount).bind(Item::getItemCount, Item::setItemCount);
        binder.forField(itemPrice).bind(Item::getItemPrice, Item::setItemPrice);
        binder.forField(itemCurrencyType).bind(Item::getItemCurrencyType, Item::setItemCurrencyType);

        itemLastUpdate.setValue(LocalDate.now());

        binder.forField(itemCode).bind(Item::getItemCode, Item::setItemCode);
        binder.forField(itemOwner).bind(Item::getItemOwner, Item::setItemOwner);
        binder.forField(itemInventoryNumber).bind(Item::getItemInventoryNumber, Item::setItemInventoryNumber);
        binder.forField(itemSerialNumber).bind(Item::getItemSerialNumber, Item::setItemSerialNumber);
        binder.forField(itemBatchNumber).bind(Item::getItemBatchNumber, Item::setItemBatchNumber);
        binder.forField(itemDivisionName).bind(Item::getItemCode, Item::setItemCode);
        binder.forField(itemLastUpdate).bind(Item::getItemLastUpdate, Item::setItemLastUpdate);
        binder.forField(itemDivisionName).bind(Item::getItemDivisionName, Item::setItemDivisionName);
        binder.forField(itemLocation).bind(Item::getItemLocation, Item::setItemLocation);
        binder.forField(itemFactoryName).bind(Item::getItemFactoryName, Item::setItemFactoryName);
        binder.forField(itemWareHouseName).bind(Item::getItemWareHouseName, Item::setItemWareHouseName);
        binder.forField(itemDescription).bind(Item::getItemDescription, Item::setItemDescription);
        // binder.forField(issueField).bind(Item::getItemIssue, Item::setItemIssue);

        add(itemDescription,
                itemSerialNumber,
                itemInventoryNumber,
                itemCode,
                itemBatchNumber,
                itemCount,
                itemPrice,
                itemCurrencyType,
                itemFactoryName,
                itemWareHouseName,
                itemDivisionName,
                itemLocation,
                itemOwner,
                itemLastUpdate,
                itemCategory,
                statusBox,
                itemIssue,
                createButtonsLayout());
    }

    public void setItem(Item item) {
        this.item = item;
        binder.readBean(item);
    }

    private HorizontalLayout createButtonsLayout() {
        save = new Button("Save", buttonClickEvent -> validateAndSave());
        close = new Button("Cancel", buttonClickEvent -> closeEditor());

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        final Item clearItem = Item.builder().build();
        try {
            binder.writeBean(clearItem);
            itemCrudService.getRepository().save(clearItem);
            gridListDataView.addItem(clearItem);
            gridListDataView.refreshItem(clearItem);
            fireEvent(new SaveEvent(this, item));
            closeEditor();
            final Notification notification = new Notification(
                    "Successfully updated item with id " + clearItem.getItemId(),
                    3000, Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.open();
        } catch (ValidationException validationException) {
            final Notification notification = new Notification(
                    "Validation data error: " + validationException.getMessage(),
                    3000, Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        } catch (Exception e) {
            log.error("Unable to update item in db", e);
            e.printStackTrace();
            final Notification notification = new Notification(
                    "Error has occurred: " + e.getMessage() + ". Please contact IT-administrator",
                    3000, Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }
        closeEditor();
    }

    private void closeEditor() {
        setItem(null);
        setVisible(false);
        removeClassName("editing");
        fireEvent(new CloseEvent(this));
    }

    public static class SaveEvent extends ItemFormEvent {
        SaveEvent(ItemForm source, Item item) {
            super(source, item);
        }
    }

    public static class CloseEvent extends ItemFormEvent {
        CloseEvent(ItemForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
            ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
