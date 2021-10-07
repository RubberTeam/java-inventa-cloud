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
package com.rubbers.team.data.generator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.entity.task.Task;
import com.rubbers.team.data.entity.task.TaskStatus;
import com.rubbers.team.data.service.ItemRepository;
import com.rubbers.team.data.service.TaskRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@SpringComponent
public class ItemDataGenerator {

    @Bean
    public CommandLineRunner loadData(final ItemRepository itemRepository, final TaskRepository taskRepository) {
        return args -> {
            for (int i = 0; i < 300; i++) {
                itemRepository.save(Item.getRandom());
            }
            log.info("Generated item list data");

            val items = itemRepository.findAll()
                    .stream()
                    .sorted(Comparator.comparing(Item::getItemId))
                    .collect(Collectors.toList());

            val task1items = new HashSet<Item>();
            for (int i = 0; i < 5; i++) {
                task1items.add(items.get(new Random().nextInt(100)));
            }
            val task1 = Task.builder()
                    .items(task1items)
                    .assignedDateTime(LocalDateTime.now())
                    .taskStatus(TaskStatus.DONE)
                    .build();
            task1.getIssues().forEach(item -> item.setTaskID(task1.getTaskId()));
            taskRepository.save(task1);

            val task2items = new HashSet<Item>();
            for (int i = 0; i < 5; i++) {
                task2items.add(items.get(new Random().nextInt((200 - 100) + 100)));
            }
            val task2 = Task.builder()
                    .items(task2items)
                    .assignedDateTime(LocalDateTime.now())
                    .taskStatus(TaskStatus.DONE)
                    .build();
            task2.getIssues().forEach(item -> item.setTaskID(task2.getTaskId()));
            taskRepository.save(task2);

            val task3items = new HashSet<Item>();
            for (int i = 0; i < 10; i++) {
                task3items.add(items.get(new Random().nextInt((300 - 200) + 200)));
            }
            val task3 = Task.builder()
                    .items(task3items)
                    .assignedDateTime(LocalDateTime.now())
                    .taskStatus(TaskStatus.ASSIGNED)
                    .build();
            task3.getIssues().forEach(item -> item.setTaskID(task3.getTaskId()));
            taskRepository.save(task3);

            val task4items = new HashSet<Item>();
            for (int i = 0; i < 10; i++) {
                task4items.add(items.get(new Random().nextInt((300 - 200) + 200)));
            }
            val task4 = Task.builder()
                    .items(task4items)
                    .taskStatus(TaskStatus.SCHEDULED)
                    .build();
            task4.getIssues().forEach(item -> item.setTaskID(task4.getTaskId()));
            taskRepository.save(task4);
        };
    }
}
