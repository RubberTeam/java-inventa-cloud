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

import java.util.Set;

import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.service.impl.TaskCrudService;
import com.rubbers.team.data.service.impl.UserCrudService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TaskDialog extends Dialog {

    public TaskDialog(@NonNull final TaskCrudService taskCrudService,
            @NonNull final UserCrudService userCrudService,
            @NonNull final Set<Item> items) {
        final TaskForm taskForm = new TaskForm(taskCrudService, userCrudService, items);
        final Button createTaskButton = new Button("Создать задачу", event -> {
            if (taskForm.validateAndSave()) {
                close();
            }
        });
        final Button cancelTaskButton = new Button("Отмена", event -> close());
        createTaskButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        final HorizontalLayout buttonLayout = new HorizontalLayout(cancelTaskButton, createTaskButton);
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        add(taskForm, buttonLayout);
    }

}
