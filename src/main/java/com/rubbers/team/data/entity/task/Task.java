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
package com.rubbers.team.data.entity.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.persistence.*;

import org.apache.commons.lang3.RandomStringUtils;

import com.rubbers.team.data.entity.issue.Issue;
import com.rubbers.team.data.entity.item.Item;

import lombok.*;

/**
 * Задача покрывающая все стадии бизнесс-процесса на стороне бэка
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    /**
     * Уникальный служебный идентификатор, не может быть null, заменяет системный номер инвентаризации
     */
    @Id
    @NonNull
    @Builder.Default
    @Column(name = "ID")
    private UUID taskId = UUID.randomUUID();

    /**
     * Ссылка на пользователя, который является администратором процесса, не может быть null, не может быть пустым
     */
    @Builder.Default
    private String businessAdmin = "admin";

    /**
     * Какие-либо контактные данные для возможного уточнения задачи
     */
    @Nullable
    @Builder.Default
    private String contactsAdmin = "88005553535 или " + RandomStringUtils.randomAlphabetic(5) + "@sberbank.ru";

    /**
     * Ссылка на исполнителя, который в текущий момент назначен на процесс
     */
    @Builder.Default
    private String assignedPerformer = "user";

    /**
     * Статус таска, не может быть null
     */
    @NonNull
    @Builder.Default
    private TaskStatus taskStatus = TaskStatus.CREATED;

    /**
     * Финансовый год, не может быть null
     */
    @Builder.Default
    private int fiscalYear = LocalDate.now().getYear();

    /**
     * Ссылка на документ, инициирующий процесс инвенторизации, может быть null, может быть пустым
     */
    @NonNull
    @Builder.Default
    private String orderDocument = "Приказ №" + RandomStringUtils.randomNumeric(3) + " о проведении учета";

    /**
     * Цифровая инвенторизация, что это вообще???
     */
    @Builder.Default
    private boolean cipherInventoried = false;

    /**
     * Время создания бизнес-задачи
     */
    @Builder.Default
    private LocalDateTime creationDateTime = LocalDateTime.now();

    /**
     * Время назначения бизнес-задачи
     */
    @Nullable
    private LocalDateTime assignedDateTime;

    /**
     * Время первого события бизнес-задачи
     */
    @Nullable
    private LocalDateTime firstActionDateTime;

    /**
     * Время завершения бизнесс-задачи
     */
    private LocalDateTime endDateTime;

    /**
     * Список ценностных объектов, участвующих в таске. Может быть пустым, правда в каких случаях?
     */
    @NonNull
    @Builder.Default
    @JoinColumn(name = "TASK_ID")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Item> items = Collections.emptySet();

    /**
     * Список проблем, которые возникли в данном бизнесс-процессе
     */
    @Builder.Default
    @JoinColumn(name = "TASK_ID")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Issue> issues = Collections.emptySet();
}
