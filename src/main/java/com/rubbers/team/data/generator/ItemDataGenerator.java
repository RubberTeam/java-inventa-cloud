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

            val task1 = Task.builder()
                    .assignedDateTime(LocalDateTime.now())
                    .taskStatus(TaskStatus.DONE)
                    .build();
            taskRepository.save(task1);
            for (int i = 0; i < 5; i++) {
                val temp = items.get(new Random().nextInt(100));
                temp.setTaskID(task1.getTaskId());
                temp.setTaskCurrentlyInventoried(false);
                itemRepository.save(temp);
            }

            val task2 = Task.builder()
                    .assignedDateTime(LocalDateTime.now())
                    .taskStatus(TaskStatus.DONE)
                    .build();
            taskRepository.save(task2);
            for (int i = 0; i < 5; i++) {
                val temp = items.get(new Random().nextInt((200 - 100) + 100));
                temp.setTaskID(task2.getTaskId());
                temp.setTaskCurrentlyInventoried(false);
                itemRepository.save(temp);
            }

            val task3 = Task.builder()
                    .assignedDateTime(LocalDateTime.now())
                    .taskStatus(TaskStatus.IN_PROGRESS)
                    .build();
            taskRepository.save(task3);
            for (int i = 0; i < 5; i++) {
                val temp = items.get(new Random().nextInt((300 - 200) + 200));
                temp.setTaskID(task3.getTaskId());
                temp.setTaskCurrentlyInventoried(true);
                itemRepository.save(temp);
            }
            val doneItemTemp = items.get(new Random().nextInt((300 - 200) + 200));
            doneItemTemp.setTaskID(task3.getTaskId());
            doneItemTemp.setTaskCurrentlyInventoried(false);
            itemRepository.save(doneItemTemp);

            val task4 = Task.builder()
                    .creationDateTime(LocalDateTime.now().minusWeeks(1))
                    .assignedDateTime(LocalDateTime.now().minusWeeks(1))
                    .taskStatus(TaskStatus.ISSUE)
                    .build();
            taskRepository.save(task4);
            for (int i = 0; i < 5; i++) {
                val temp = items.get(new Random().nextInt((300 - 200) + 200));
                temp.setTaskID(task4.getTaskId());
                temp.setTaskCurrentlyInventoried(false);
                itemRepository.save(temp);
            }
            val notDoneItemTemp = items.get(new Random().nextInt((300 - 200) + 200));
            notDoneItemTemp.setTaskID(task4.getTaskId());
            notDoneItemTemp.setTaskCurrentlyInventoried(true);
            itemRepository.save(notDoneItemTemp);
        };
    }
}
