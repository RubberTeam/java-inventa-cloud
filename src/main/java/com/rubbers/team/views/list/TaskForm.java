package com.rubbers.team.views.list;

import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.entity.task.Task;
import com.rubbers.team.data.entity.task.TaskStatus;
import com.rubbers.team.data.entity.user.User;
import com.rubbers.team.data.service.impl.TaskCrudService;
import com.rubbers.team.data.service.impl.UserCrudService;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Форма для создания тасков из таблицы с ценностными объектами
 */
public class TaskForm extends FormLayout {
    final Binder<Task> binder = new BeanValidationBinder<>(Task.class);

    private UserCrudService userCrudService;
    private TaskCrudService taskCrudService;
    private List<Item> items;

    private TextField idField = new TextField("ID");
    private TextField itemsCount = new TextField("Количество объектов");
    private ComboBox<User> fromUser = new ComboBox<>("От полльзователя");
    private ComboBox<User> toUser = new ComboBox<>("Для пользователя");
    private TextField contactInformation = new TextField("Контактная информация ответственного лица");
    private ComboBox<TaskStatus> initialStatus = new ComboBox<>("Начальный статус");
    private DatePicker fiscalYearPicker = new DatePicker("Фискальный год");
    private TextField orderDocument = new TextField("Документ-обоснование");
    private Checkbox cipher = new Checkbox("Цифровая инвентаризация");

    /**
     * Дефолтный конструктор формы
     *
     * @param taskCrudService сервис, куда сохраним созданный таск
     * @param items           список ценностных объектов для бизнесс-процесса
     */
    public TaskForm(final TaskCrudService taskCrudService, final UserCrudService userCrudService, final List<Item> items) {
        addClassName("task-form");

        this.taskCrudService = taskCrudService;
        this.items = items;

        idField.setReadOnly(true);
        idField.setValue(UUID.randomUUID().toString());
        itemsCount.setValue(String.valueOf(items.size()));
        fromUser.setItems(userCrudService.getRepository().findAll());
        toUser.setItems(userCrudService.getRepository().findAll());
        contactInformation.setPlaceholder("Укажите контактную информацию");
        initialStatus.setItems(TaskStatus.values());
        fiscalYearPicker.setValue(LocalDate.now());

        final List<Integer> selectableYears = IntStream.range(
                        LocalDate.now().getYear() - 99,
                        LocalDate.now().getYear() + 1)
                .boxed().collect(Collectors.toList());
        //fiscalYearPicker.
    }

}
