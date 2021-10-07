package com.rubbers.team.views.list;

import com.rubbers.team.data.entity.item.Item;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

/**
 * Вспомогательный класс-ивентер для форм поверх основных лэйаутов
 */
public class ItemFormEvent extends ComponentEvent<ItemForm> {

    @Getter
    private final Item item;

    public ItemFormEvent(ItemForm source, Item item) {
        super(source, false);
        this.item = item;
    }
}
