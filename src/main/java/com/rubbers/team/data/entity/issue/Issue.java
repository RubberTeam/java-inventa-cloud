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
package com.rubbers.team.data.entity.issue;

import java.util.UUID;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

import lombok.*;

/**
 * Сущность объекта, который описывает какие-либо проблемы бизнес-процесса
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Issue {

    /**
     * Уникальный служебный идентификатор
     */
    @Id
    @NonNull
    @Builder.Default
    private UUID id = UUID.randomUUID();

    /**
     * Описание проблемы
     */
    @NotBlank
    @NonNull
    private String description = "Есть какая-то проблема";

    /**
     * Идентификатор проблемы в других системах, например, сбердруг
     */
    @Nullable
    private String identifierThirdPartySystems;

    /**
     * Подтверждение
     */
    @Lob
    @Nullable
    private byte[] pic;

}
