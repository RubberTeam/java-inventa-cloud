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

import com.rubbers.team.views.MainLayout;
import com.rubbers.team.views.cardlist.Person;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Leaders List")
@Route(value = "leaders-list", layout = MainLayout.class)
@PermitAll
public class LeadersView extends Div implements AfterNavigationObserver {

    private Div weekPage;
    private Div monthPage;

    Grid<Person> grid = new Grid<>();

    public LeadersView() {
        addClassName("leaders-view");
        setSizeFull();
        addTabs();
        configureWeekPage();
        configureMonthPage();

    }

    private void configureMonthPage() {
        grid.setHeight("1000%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(person -> createCard(person));
        add(grid);
    }

    private void configureWeekPage() {
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(person -> createCard(person));
    }

    private void addTabs() {
        final Tab weekLeadTab = new Tab("Лидеры недели");
        weekPage = new Div();

        final Tab monthLeadTab = new Tab("Лидеры месяца");
        monthPage = new Div();
        monthPage.setVisible(false);

        final Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(weekLeadTab, weekPage);
        tabsToPages.put(monthLeadTab, monthPage);

        final Tabs tabs = new Tabs(weekLeadTab, monthLeadTab);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.setFlexGrowForEnclosedTabs(1);
        final Div pages = new Div(weekPage, monthPage);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            final Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, pages);
    }

    private HorizontalLayout createCard(Person person) {
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

        Span post = new Span(person.getPost());
        post.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon likeIcon = VaadinIcon.HEART.create();
        likeIcon.addClassName("icon");
        Span likes = new Span(person.getLikes());
        likes.addClassName("likes");

        actions.add(likeIcon, likes);

        description.add(header, post, actions);
        card.add(image, description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Set some data when this view is displayed.
        List<Person> persons = Arrays.asList( //
                createPerson("https://randomuser.me/api/portraits/men/42.jpg", "John Smith", "May 8",
                        "1K"),
                createPerson("https://randomuser.me/api/portraits/women/42.jpg", "Abagail Libbie", "May 3",
                        "1K"),
//                createPerson("https://randomuser.me/api/portraits/men/24.jpg", "Alberto Raya", "May 3",
//                        "1K"),
//                createPerson("https://randomuser.me/api/portraits/women/24.jpg", "Emmy Elsner", "Apr 22",
//                        "1K"),
//                createPerson("https://randomuser.me/api/portraits/men/76.jpg", "Alf Huncoot", "Apr 21",
//                        "1K"),
//                createPerson("https://randomuser.me/api/portraits/women/76.jpg", "Lidmila Vilensky", "Apr 17",
//                        "1K"),
//                createPerson("https://randomuser.me/api/portraits/men/94.jpg", "Jarrett Cawsey", "Apr 17",
//                        "1K"),
                createPerson("https://randomuser.me/api/portraits/women/94.jpg", "Tania Perfilyeva", "Mar 8",
                        "1K"),
                createPerson("https://randomuser.me/api/portraits/men/16.jpg", "Ivan Polo", "Mar 5",
                        "1K"),
                createPerson("https://randomuser.me/api/portraits/women/16.jpg", "Emelda Scandroot", "Mar 5",
                        "1K"),
                createPerson("https://randomuser.me/api/portraits/men/67.jpg", "Marcos Sá", "Mar 4",
                        "1K"),
                createPerson("https://randomuser.me/api/portraits/women/67.jpg", "Jacqueline Asong", "Mar 2",
                        "1K")

        );
        grid.setItems(persons);
    }

    private static Person createPerson(String image, String name, String date, String likes) {
        Person p = new Person();
        p.setImage(image);
        p.setName(name);
        p.setDate(date);
        p.setLikes(likes);
        return p;
    }

}
