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
package com.rubbers.team.views.list.item;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.Nullable;

import com.rubbers.team.data.entity.audit.Audit;
import com.rubbers.team.data.entity.issue.Issue;
import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.entity.item.ItemCategory;
import com.rubbers.team.data.entity.item.ItemStatus;
import com.rubbers.team.data.entity.user.User;
import com.rubbers.team.data.service.impl.AuditCrudService;
import com.rubbers.team.data.service.impl.ItemCrudService;
import com.rubbers.team.data.service.impl.UserCrudService;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Форма для редактирования и создания ценностных объектов
 */
@Slf4j
public class ItemForm extends FormLayout {
    private final Binder<Item> binder = new BeanValidationBinder<>(Item.class);

    private final ItemCrudService itemCrudService;
    private final AuditCrudService auditCrudService;
    private final GridListDataView<Item> gridListDataView;
    private final Item item;

    public ItemForm(@NonNull final ItemCrudService itemCrudService,
            @NonNull final UserCrudService userCrudService,
            @NonNull final AuditCrudService auditCrudService,
            @NonNull final GridListDataView<Item> gridListDataView,
            @Nullable final Item item) {
        this.itemCrudService = itemCrudService;
        this.gridListDataView = gridListDataView;
        this.auditCrudService = auditCrudService;
        this.item = item;

        addClassName("item-form");

        final ComboBox<ItemCategory> itemCategory = new ComboBox<>("Категория ценности");
        itemCategory.setItems(ItemCategory.values());
        binder.forField(itemCategory).bind(Item::getItemCategory, Item::setItemCategory);

        final ComboBox<ItemStatus> statusBox = new ComboBox<>("Статус");
        statusBox.setItems(ItemStatus.values());
        binder.forField(statusBox).bind(Item::getItemStatus, Item::setItemStatus);

        final Checkbox itemIsUsed = new Checkbox("БУ");
        itemIsUsed.setValue(false);
        binder.forField(itemIsUsed).bind(Item::getItemIsUsed, Item::setItemIsUsed);

        final IntegerField itemCount = new IntegerField("Количество предметов");
        itemCount.setValue(1);
        itemCount.setMin(1);
        itemCount.setHasControls(true);
        binder.forField(itemCount).bind(Item::getItemCount, Item::setItemCount);

        final IntegerField itemPrice = new IntegerField("Стоимость");
        itemCount.setHasControls(true);
        binder.forField(itemPrice).bind(Item::getItemPrice, Item::setItemPrice);

        final TextField itemCurrencyType = new TextField("Код валюты");
        binder.forField(itemCurrencyType).bind(Item::getItemCurrencyType, Item::setItemCurrencyType);

        final DatePicker itemLastUpdate = new DatePicker("Дата последнего обновления");
        itemLastUpdate.setValue(LocalDate.now());

        final TextField itemCode = new TextField("Табельный номер");
        itemCode.setPlaceholder("Укажите табельный номер");
        binder.forField(itemCode).bind(Item::getItemCode, Item::setItemCode);

        // Отличная идея с хреновой реализацией
        // final ComboBox<User> itemOwner = new ComboBox<>("Ответственный за объект, например МОЛ на месте или
        // складе)");
        // itemOwner.setItems(userCrudService.getRepository().findAll());
        // binder.forField(itemOwner)
        // .bind(
        // x -> userCrudService.getRepository()
        // .findAll()
        // .stream()
        // .filter(u -> u.getUsername().equalsIgnoreCase(x.getItemOwner()))
        // .findAny()
        // .orElse(null), // todo добавить метод поиска по логину в CrudUserService
        // (x, y) -> x.setItemOwner(y.getUsername())
        // );
        final TextField itemOwner = new TextField("Ответственный за объект, например МОЛ на месте или складе)");
        binder.forField(itemOwner).bind(Item::getItemOwner, Item::setItemOwner);

        final TextField itemInventoryNumber = new TextField("Инвентарный номер");
        binder.forField(itemInventoryNumber).bind(Item::getItemInventoryNumber, Item::setItemInventoryNumber);

        final TextField itemSerialNumber = new TextField("Серийный номер");
        binder.forField(itemSerialNumber).bind(Item::getItemSerialNumber, Item::setItemSerialNumber);

        final TextField itemBatchNumber = new TextField("Номер партии");
        binder.forField(itemBatchNumber).bind(Item::getItemBatchNumber, Item::setItemBatchNumber);

        final TextField itemDivisionName = new TextField("Подразделение");
        binder.forField(itemDivisionName).bind(Item::getItemCode, Item::setItemCode);
        binder.forField(itemLastUpdate).bind(Item::getItemLastUpdate, Item::setItemLastUpdate);
        binder.forField(itemDivisionName).bind(Item::getItemDivisionName, Item::setItemDivisionName);

        final TextField itemLocation = new TextField("Адрес");
        binder.forField(itemLocation).bind(Item::getItemLocation, Item::setItemLocation);

        final TextField itemFactoryName = new TextField("Название завода");
        binder.forField(itemFactoryName).bind(Item::getItemFactoryName, Item::setItemFactoryName);

        final TextField itemWareHouseName = new TextField("Название склада");
        binder.forField(itemWareHouseName).bind(Item::getItemWareHouseName, Item::setItemWareHouseName);

        final TextArea itemDescription = new TextArea("Описание");
        itemDescription.setPlaceholder("Уточните описание ценностного объекта, например имя ТМЦ");
        binder.forField(itemDescription).bind(Item::getItemDescription, Item::setItemDescription);

        final TextField itemTaskID = new TextField("Последняя задача по объекту");
        itemTaskID.setReadOnly(true);
        binder.forField(itemTaskID).bind(
                x -> {
                    if (x.getTaskID() != null) {
                        return x.getTaskID().toString();
                    } else {
                        return "";
                    }
                },
                (x, y) -> {
                    if (y == null || "".equals(y)) {
                        x.setTaskID(null);
                    } else {
                        x.setTaskID(UUID.fromString(y));
                    }
                });

        // todo подумать, как сделать лучше. Сейчас не отображается
        final ComboBox<Issue> itemIssue = new ComboBox<>("Проблема");
        binder.forField(itemIssue).bind(Item::getItemIssue, Item::setItemIssue);

        if (item != null) {
            binder.readBean(item);
        }

        add(
                itemDescription,
                statusBox,
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
                itemTaskID,
                itemCategory
        // itemIssue
        );
    }

    public boolean validateAndSave() {
        try {
            if (item != null) {
                binder.writeBean(item);
                itemCrudService.getRepository().save(item);
                gridListDataView.addItem(item);
                gridListDataView.refreshItem(item);
                auditCrudService.getRepository().save(new Audit(
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        "Бизнес-администратором изменен объект ID: " + item.getItemId()));
            } else {
                final Item clearItem = Item.builder().build();
                binder.writeBean(clearItem);
                itemCrudService.getRepository().save(clearItem);
                gridListDataView.addItem(clearItem);
                gridListDataView.refreshItem(clearItem);
                auditCrudService.getRepository().save(new Audit(
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        "Бизнес-администратором создан новый объект ID: " + clearItem.getItemId()));
            }
            final Notification notification = new Notification(
                    item == null ? "Объект успешно создан" : "Объект успешно обновлен",
                    3000,
                    Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.open();
            return true;
        } catch (ValidationException validationException) {
            final Notification notification = new Notification(
                    "Ошибка при заполнении данных, проверьте введенные данные!",
                    3000,
                    Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
            return false;
        } catch (Exception e) {
            log.error("Unexpected exception on task creation", e);
            final Notification notification = new Notification(
                    "Error has occurred: " + e.getMessage() + ". Please contact IT-administrator",
                    3000,
                    Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
            return false;
        }
    }
}
