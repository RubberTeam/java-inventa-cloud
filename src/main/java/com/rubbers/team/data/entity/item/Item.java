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
package com.rubbers.team.data.entity.item;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

/**
 * Сущность объекта, который когда-либо подлежал или будет подлежать инвентаризации
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    /**
     * Соответствует item_ID
     */
    @Id
    @NonNull
    @Builder.Default
    private UUID itemId = UUID.randomUUID();

    /**
     * Соответствует Category_Name
     *
     * @see ItemCategory
     */
    @Builder.Default
    private ItemCategory itemCategory = ItemCategory.OTHER;

    /**
     * Соответствует Task_ID - последний активный таск, в котором велась работы над данным объектом
     */
    private String taskId;

    /**
     * Соответствует Item_Name - имя и описание сущности
     */
    private String itemDescription;

    /**
     * Соответствует Count - количество предметов подразумеваемых под данной сущностью
     */
    @Builder.Default
    private int itemCount = 1;

    /**
     * Соответствует Inventory_Number
     */
    private String itemInventoryNumber;

    /**
     * Соответствует Barcode - код, который содержится в баркоде или qr-коде
     */
    private String itemCode;

    /**
     * Соответствует Status - код состояния
     *
     * @see ItemStatus
     */
    private ItemStatus itemStatus;

    /**
     * Соответствует 'Adress' - адресу + уточнение локаци, например кабинет и рабочее место
     */
    private String itemLocation;

    /**
     * Дата последнего обновления
     */
    private LocalDate itemLastUpdate;

    /**
     * Какая-либо информация о человеке, ответственном за объект
     */
    private String itemOwner;

    /**
     * Соответствует Comment - подробный текст комментария по проблеме, возможно айдишник на ищью в другой системе
     */
    private String itemIssue;

    public static Item getRandom() {
        val status = getRandomStatus();
        return Item.builder()
                .itemCode(RandomStringUtils.randomNumeric(10))
                .itemDescription(getRandomDescription())
                .itemLastUpdate(getRandomLastDate())
                .itemCount(getRandomCount())
                .itemStatus(status)
                .itemIssue(getRandomIssueID(status))
                .itemLocation(getRandomLocation())
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

    private static String getRandomLocation() {
        val list = Stream.of(
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 6" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 6" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 6" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 6" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 7" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 7" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 7" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 7" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 8" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 8" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 8" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, площадь Фаберже 8, Литера 2, 8" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 1" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 1" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 1" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 2" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 2" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 2" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 3" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 3" + RandomStringUtils.randomNumeric(2),
                "Санкт-Петербург, Красных Текстильщиков 2, 3" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 28" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 28" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 28" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 28" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 29" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 29" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 29" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 29" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 30" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 30" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 30" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 30" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 31" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 31" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 31" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 31" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 32" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 32" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 32" + RandomStringUtils.randomNumeric(2),
                "Москва, Кутузовский 32, Литера 1, 32" + RandomStringUtils.randomNumeric(2))
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

    private static ItemStatus getRandomStatus() {
        switch (new Random().nextInt(4)) {
            case 0:
                return ItemStatus.OK;
            case 1:
                return ItemStatus.OUT_OF_SERVICE;
            case 3:
                return ItemStatus.ISSUE;
            default:
                return ItemStatus.MISSED;
        }
    }

    private static String getRandomIssueID(final ItemStatus status) {
        if (new Random().nextInt(3) != 2) {
            return null;
        } else {
            if (!status.equals(ItemStatus.OK)) {
                return "SD" + RandomStringUtils.randomNumeric(10);
            } else {
                return null;
            }
        }
    }

}
