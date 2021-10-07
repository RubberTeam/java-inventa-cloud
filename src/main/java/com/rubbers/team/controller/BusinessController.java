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
package com.rubbers.team.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rubbers.team.controller.service.TaskService;
import com.rubbers.team.data.entity.event.Event;
import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.entity.item.ItemCategory;
import com.rubbers.team.data.entity.task.Task;
import com.rubbers.team.data.service.impl.ItemCrudService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController("/task")
public class BusinessController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ItemCrudService itemCrudService;

    // @Autowired
    // private EventRepository eventRepository;

    @ResponseBody
    @GetMapping("/getTasksByUser")
    public List<Task> getTasksByUser(@RequestParam(name = "user") @NotBlank final String user) {
        return taskService.getTasksByUser(user);
    }

    @PostMapping("/update")
    public void update(@RequestBody @NotBlank final Event event) {
        // eventRepository.save(event);
        // taskService.update(task);
    }

    @ResponseBody
    @GetMapping("/getItems")
    public List<Item> getItemsByFilters(
            @RequestParam(name = "location", required = false) final String location,
            @RequestParam(name = "owner", required = false) final String owner,
            @RequestParam(name = "division", required = false) final String division,
            @RequestParam(name = "category", required = false) final ItemCategory itemCategory,
            @RequestParam(name = "inventory", required = false) final String inventoryNumber,
            @RequestParam(name = "serial", required = false) final String serialNumber) {
        return itemCrudService.getRepository()
                .findAll()
                .stream()
                .limit(20)
                .collect(Collectors.toList());
    }
}
