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

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @NonNull
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String serialNumber;
    private String description;
    private LocalDate lastUpdate;
    private String lastTask;
    @Builder.Default
    private int count = 1;
    private String status;
    private String location;
    private String mapLocation;
    private String issue;

    public static Item getRandom() {
        return Item.builder()
                .serialNumber(RandomStringUtils.randomNumeric(10))
                .description(getRandomDescription())
                .lastUpdate(getRandomLastDate())
                .count(getRandomCount())
                .status(getRandomStatus())
                .build();
    }

    private static String getRandomDescription() {
        val list = Stream.of(
                        "Пирожок в ресторане", "Коньяк в тумбочке", "Пачка зеленых ручек", "Очень важные бумаги",
                        "Несуществующая премия", "Минимальный коэффициент премии", "Резиновые люди", "Ктсы за 300",
                        "Сервисное ядро", "Командировка только после вакцины", "Просто мда", "Ну что тут сказать",
                        "Теннисный стол шириной в кухню", "Печеньки и кофе за свой счет, вам и так много платят",
                        "Бог в помощь", "Вам НТ не нужно", "Командировка в питер", "Командировка в Москву",
                        "РОР не лежит на ИФТ, а устанавливается", "Это у вас проблемы, а не у нас",
                        "Оценка Б для всей команды", "Никогда не было, и вот опять РОР не отвечает",
                        "Билет на все конференции JUG.RU, но в следующем году, в этом уже не успеем")
                .collect(Collectors.toList());
        return list.get(new Random().nextInt(list.size()));
    }

    private static LocalDate getRandomLastDate() {
        val rand = new Random();
        return LocalDate.of(
                rand.nextInt((2021 - 2019) + 1) + 2019,
                rand.nextInt((12 - 1) + 1) + 1,
                rand.nextInt((25 - 1) + 1) + 1);
    }

    private static int getRandomCount() {
        if (new Random().nextInt(3) != 2) {
            return 1;
        } else {
            return new Random().nextInt((1000 - 1) + 1) + 1;
        }
    }

    private static String getRandomStatus() {
        switch (new Random().nextInt(4)) {
            case 0:
                return "ok";
            case 1:
                return "out of service";
            case 3:
                return "issue";
            default:
                return "missed";
        }
    }
}
