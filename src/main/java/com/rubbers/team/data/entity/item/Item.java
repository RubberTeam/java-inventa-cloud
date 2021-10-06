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
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;

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
     * Соответствует item_ID, уникальный служебный идентификатор, не может быть null
     */
    @Id
    @NonNull
    @Builder.Default
    private UUID itemId = UUID.randomUUID();

    /**
     * Соответствует Category_Name, категория ценностного объекта, не может быть null, не может быть пустым
     *
     * @see ItemCategory
     */
    @NonNull
    @Builder.Default
    private ItemCategory itemCategory = ItemCategory.OTHER;

    /**
     * Соответствует Status - состояние ценностного объекта, не может быть null, не может быть пустым
     *
     * @see ItemStatus
     */
    @NonNull
    @Builder.Default
    private ItemStatus itemStatus = ItemStatus.IN_USE;

    /**
     * Признак, что ценностный объект когда-либо уже использовался
     */
    @Builder.Default
    private Boolean itemIsUsed = true;

    /**
     * Фактическое подразделение, в котором находится объект, или к которому приписан. Соответствует DIVISION, может
     * быть пустым или null
     */
    private String itemDivisionName;

    /**
     * Название завода, где произведен или произведен ремонт ценностного объекта, может быть null, может быть пустым.
     * Соответствует WERKS
     */
    private String itemFactoryName;

    /**
     * Название склада, где размещается ценностный объект, соответствует LGORT
     */
    private String itemWareHouseName;

    /**
     * Соответствует ZZPERNR - табельный номер, который содержится в баркоде или qr-коде, может быть null, может быть
     * пустым
     */
    private String itemCode;

    /**
     * МОЛ, ответственный за объект, может быть null, может быть пустым. Отмечается фактический человек на месте.
     * Соответствует LINKK
     */
    private String itemOwner;

    /**
     * Соответствует INVNR - инвентарный номер, может быть null, может быть пустым
     */
    private String itemInventoryNumber;

    /**
     * Соответствует SERIAL - серийный номер, может быть null, может быть пустым
     */
    private String itemSerialNumber;

    /**
     * Соответствует CHARG - номер партии, может быть null, может быть пустым
     */
    private String itemBatchNumber;

    /**
     * Соответствует TMC_NAME - имя и описание сущности (товарно-материальные-ценности)
     */
    private String itemDescription;

    /**
     * Соответствует Task_ID - последний активный таск, в котором велась работы над данным объектом
     */
    private String taskId;

    /**
     * Индикатор того, что уже находится в каком-то таске И должен быть проинвенторизирован
     */
    @Builder.Default
    private Boolean taskCurrentlyInventoried = false;

    /**
     * Количество предметов подразумеваемых под данной сущностью, соответствует BU_VALUE
     * (Количество по БУ), по дефолту 1, не может быть меньше 1
     */
    @Min(1)
    @Builder.Default
    private int itemCount = 1;

    /**
     * Соответствует BU_AMOUNT - стоимость по БУ, 0 в случае отсутствия цены по какой-либо причине
     */
    @Min(0)
    @Builder.Default
    private int itemPrice = 0;

    /**
     * Соответствует WAERS - код валюты для цены, может быть null, может быть empty в случае отсутствия itemPrice
     */
    @Builder.Default
    private String itemCurrencyType = "RUB";

    /**
     * Соответствует 'Adress' - адресу + уточнение локаци, например кабинет и рабочее место
     */
    private String itemLocation;

    /**
     * Дата последнего обновления
     */
    private LocalDate itemLastUpdate;

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
        val statuses = Arrays.asList(ItemStatus.values());
        return statuses.get(new Random().nextInt(statuses.size()));
    }

    private static String getRandomIssueID(final ItemStatus status) {
        if (new Random().nextInt(3) != 2) {
            return null;
        } else {
            if (!status.equals(ItemStatus.IN_USE)) {
                return "SD" + RandomStringUtils.randomNumeric(10);
            } else {
                return null;
            }
        }
    }

}
