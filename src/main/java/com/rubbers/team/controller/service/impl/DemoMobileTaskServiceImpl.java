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
package com.rubbers.team.controller.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.rubbers.team.controller.service.TaskService;
import com.rubbers.team.data.entity.task.Task;
import com.rubbers.team.data.service.TaskRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@SpringComponent
@AllArgsConstructor
public class DemoMobileTaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    public List<Task> getTasksByUser(@NotBlank final String user) {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getAssignedPerformer().equalsIgnoreCase(user))
                .collect(Collectors.toList());
    }

    public void update(@NonNull final Task task) {
        taskRepository.save(task);
    }
}
