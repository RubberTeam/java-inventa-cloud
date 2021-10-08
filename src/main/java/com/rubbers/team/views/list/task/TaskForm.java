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
package com.rubbers.team.views.list.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.rubbers.team.data.entity.audit.Audit;
import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.entity.task.Task;
import com.rubbers.team.data.entity.task.TaskStatus;
import com.rubbers.team.data.entity.user.User;
import com.rubbers.team.data.service.impl.AuditCrudService;
import com.rubbers.team.data.service.impl.ItemCrudService;
import com.rubbers.team.data.service.impl.TaskCrudService;
import com.rubbers.team.data.service.impl.UserCrudService;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import lombok.extern.slf4j.Slf4j;

/**
 * Форма для создания тасков из таблицы с ценностными объектами
 */
@Slf4j
public class TaskForm extends FormLayout {
    final Binder<Task> binder = new BeanValidationBinder<>(Task.class);

    private final TaskCrudService taskCrudService;
    private final ItemCrudService itemCrudService;
    private final AuditCrudService auditCrudService;
    private final Set<Item> items;

    /**
     * Дефолтный конструктор формы
     *
     * @param taskCrudService сервис, куда сохраним созданный таск
     * @param items список ценностных объектов для бизнесс-процесса
     */
    public TaskForm(final TaskCrudService taskCrudService,
            final ItemCrudService itemCrudService,
            final UserCrudService userCrudService,
            final AuditCrudService auditCrudService,
            final Set<Item> items) {
        addClassName("task-form");

        this.taskCrudService = taskCrudService;
        this.itemCrudService = itemCrudService;
        this.auditCrudService = auditCrudService;
        this.items = items;

        final TextField idField = new TextField("ID");
        idField.setReadOnly(true);
        idField.setValue(UUID.randomUUID().toString());
        binder.forField(idField).bind(x -> x.getTaskId().toString(), (x, y) -> x.setTaskId(UUID.fromString(y)));

        final TextField itemsCount = new TextField("Количество выбранных объектов");
        if (items != null) {
            itemsCount.setValue(String.valueOf(items.size()));
        } else {
            itemsCount.setValue("0");
        }
        itemsCount.setReadOnly(true);

        final ComboBox<User> fromUser = new ComboBox<>("От полльзователя");
        fromUser.setItems(userCrudService.getRepository().findAll());
        binder.forField(fromUser)
                .bind(
                        x -> userCrudService.getRepository()
                                .findAll().stream()
                                .filter(u -> u.getUsername().equalsIgnoreCase(x.getBusinessAdmin()))
                                .findAny()
                                .get(), // todo добавить метод поиска по логину в CrudUserService
                        (x, y) -> x.setBusinessAdmin(y.getUsername()));
        final ComboBox<User> toUser = new ComboBox<>("Для пользователя");
        toUser.setItems(userCrudService.getRepository().findAll());
        binder.forField(fromUser)
                .bind(
                        x -> userCrudService.getRepository()
                                .findAll().stream()
                                .filter(u -> u.getUsername().equalsIgnoreCase(x.getAssignedPerformer()))
                                .findAny()
                                .get(), // todo добавить метод поиска по логину в CrudUserService
                        (x, y) -> x.setAssignedPerformer(y.getUsername()));

        final TextField contactInformation = new TextField("Контактная информация ответственного лица");
        contactInformation.setPlaceholder("Укажите контактную информацию");
        binder.forField(contactInformation).bind(Task::getContactsAdmin, Task::setContactsAdmin);

        final ComboBox<TaskStatus> initialStatus = new ComboBox<>("Начальный статус");
        initialStatus.setItems(TaskStatus.values());
        initialStatus.setValue(TaskStatus.ASSIGNED);
        binder.forField(initialStatus).bind(Task::getTaskStatus, Task::setTaskStatus);

        final ComboBox<Integer> fiscalYearPicker = new ComboBox<>(
                "Фискальный год",
                IntStream.range(
                        LocalDate.now().getYear() - 99,
                        LocalDate.now().getYear() + 1).boxed()
                        .collect(Collectors.toList()));
        fiscalYearPicker.setValue(LocalDate.now().getYear());
        binder.forField(fiscalYearPicker).bind(Task::getFiscalYear, Task::setFiscalYear);

        final TextField orderDocument = new TextField("Документ-обоснование");
        orderDocument.setPlaceholder("Укажите документ-обоснование");
        binder.forField(orderDocument).bind(Task::getOrderDocument, Task::setOrderDocument);

        final Checkbox cipher = new Checkbox("Цифровая инвентаризация");
        cipher.setValue(false);
        binder.forField(cipher).bind(Task::isCipherInventoried, Task::setCipherInventoried);

        add(idField,
                itemsCount,
                fromUser,
                toUser,
                contactInformation,
                initialStatus,
                fiscalYearPicker,
                orderDocument,
                cipher);
    }

    public boolean validateAndSave() {
        if (items == null) {
            final Notification notification = new Notification(
                    "Не выбрано ни одного объекта для инициации процесса инвентаризации",
                    3000,
                    Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
            return false;
        }
        try {
            final Task clearItem = Task.builder().build();
            binder.writeBean(clearItem);
            taskCrudService.getRepository().save(clearItem);
            auditCrudService.getRepository().save(new Audit(
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    "Бизнес-администратором создана новая задача ID: " + clearItem.getTaskId()));
            items.forEach(item -> {
                item.setTaskID(clearItem.getTaskId());
                item.setTaskCurrentlyInventoried(true);
                itemCrudService.getRepository().save(item);
                auditCrudService.getRepository().save(new Audit(
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        "Бизнес-администратор привязал объект ID: " + item.getItemId() + " к задаче ID: "
                                + clearItem.getTaskId()));
            });
            final Notification notification = new Notification(
                    "Задача успешно создана",
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
                    "Произошла ошибка: " + e.getMessage()
                            + ". Убедительно просим обратиться к вашем IT-Администратору",
                    3000,
                    Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
            return false;
        }
    }
}
