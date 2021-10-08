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
package com.rubbers.team.views.leaders;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.PermitAll;

import com.rubbers.team.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Leaders List")
@Route(value = "leaders-list", layout = MainLayout.class)
@PermitAll
public class LeadersView extends Div implements AfterNavigationObserver {

    private Tab weekPage;
    private Tab monthPage;
    private Tab countPage;
    private VerticalLayout content;

    Grid<PersonUser> gridWeek = new Grid<>();
    Grid<PersonUser> gridMonth = new Grid<>();
    Grid<PersonUser> gridCount = new Grid<>();

    public LeadersView() {
        addClassName("leaders-view");
        setSizeFull();
        addTabs();
    }

    private void setGrid(Grid<PersonUser> grid) {
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);

        if (grid.equals(gridWeek)) {
            grid.addComponentColumn(person -> createCard(person, 1));
        } else if (grid.equals(gridMonth)) {
            grid.addComponentColumn(person -> createCard(person, 2));
        } else {
            grid.addComponentColumn(person -> createCard(person, 3));
        }
        add(grid);
    }

    private void addTabs() {
        weekPage = new Tab("Лидеры недели");
        monthPage = new Tab("Лидеры месяца");
        countPage = new Tab("Лидеры за все время");

        Tabs tabs = new Tabs(weekPage, monthPage, countPage);
        tabs.addSelectedChangeListener(event -> setContent(event.getSelectedTab()));

        content = new VerticalLayout();
        content.setSpacing(false);
        setContent(tabs.getSelectedTab());

        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
        add(tabs, content);
    }

    private void setContent(Tab tab) {
        content.removeAll();
        if (tab.equals(weekPage)) {
            content.add(new Paragraph("This is the weekPage tab"));
            setGrid(gridWeek);
        } else if (tab.equals(monthPage)) {
            content.add(new Paragraph("This is the monthPage tab"));
            setGrid(gridMonth);
        } else {
            content.add(new Paragraph("This is the countPage tab"));
            setGrid(gridCount);
        }
    }

    private HorizontalLayout createCard(PersonUser person, int code) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc(person.getImage());
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(person.getName());
        name.addClassName("name");
        Span date = new Span(person.getDate());
        date.addClassName("date");
        header.add(name, date);

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon barcodeIcon = VaadinIcon.BARCODE.create();
        barcodeIcon.addClassName("icon");
        Span barcode = new Span();
        if (code == 1) {
            barcode.setText(String.valueOf(person.getWeekCount()));
        } else if (code == 2) {
            barcode.setText(String.valueOf(person.getMonthCount()));
            barcode.addClassName("barcode");
        } else if (code == 3) {
            barcode.setText(String.valueOf(person.getCount()));
        }
        barcode.addClassName("barcode");

        actions.add(barcodeIcon, barcode);
        description.add(header, actions);
        card.add(image, description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        // Set some data when this view is displayed.
        List<PersonUser> persons = Arrays.asList( //
                createPerson("https://randomuser.me/api/portraits/men/76.jpg", "Alf Huncoot", "Apr 21",
                        989797, 5456, 998),
                createPerson("https://randomuser.me/api/portraits/men/42.jpg", "John Smith", "May 8",
                        987, 654, 321),
                createPerson("https://randomuser.me/api/portraits/women/42.jpg", "Abagail Libbie", "May 3",
                        876, 543, 123),
                createPerson("https://randomuser.me/api/portraits/men/24.jpg", "Alberto Raya", "May 3",
                        323, 231, 121),
                createPerson("https://randomuser.me/api/portraits/women/24.jpg", "Emmy Elsner", "Apr 22",
                        434, 213, 45),
                createPerson("https://randomuser.me/api/portraits/women/94.jpg", "Tania Perfilyeva", "Mar 8",
                        98, 23, 1));
        gridWeek.setItems(persons);
        gridMonth.setItems(persons);
        gridCount.setItems(persons);
    }

    private static PersonUser createPerson(String image, String name, String date, int count, int monthCount,
            int weekCount) {
        PersonUser p = new PersonUser();
        p.setImage(image);
        p.setName(name);
        p.setDate(date);
        p.setCount(count);
        p.setWeekCount(weekCount);
        p.setMonthCount(monthCount);
        return p;
    }

}
