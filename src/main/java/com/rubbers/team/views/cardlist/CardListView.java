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
package com.rubbers.team.views.cardlist;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;

import com.rubbers.team.data.entity.task.Task;
import com.rubbers.team.data.entity.user.User;
import com.rubbers.team.data.service.impl.IssueCrudService;
import com.rubbers.team.data.service.impl.ItemCrudService;
import com.rubbers.team.data.service.impl.TaskCrudService;
import com.rubbers.team.data.service.impl.UserCrudService;
import com.rubbers.team.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.AllArgsConstructor;
import lombok.NonNull;

// @PermitAll
// @PageTitle("Card List")
// @Route(value = "card-list", layout = MainLayout.class)
public class CardListView extends Div {

    final ItemCrudService itemCrudService;
    final UserCrudService userCrudService;
    final IssueCrudService issueCrudService;
    final TaskCrudService taskCrudService;

    final Grid<Task> grid = new Grid<>();

    public CardListView(@NonNull final ItemCrudService itemCrudService,
            @NonNull final UserCrudService userCrudService,
            @NonNull final IssueCrudService issueCrudService,
            @NonNull final TaskCrudService taskCrudService) {
        this.itemCrudService = itemCrudService;
        this.userCrudService = userCrudService;
        this.taskCrudService = taskCrudService;
        this.issueCrudService = issueCrudService;

        addClassName("card-list-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        // grid.addComponentColumn(person -> createTaskInfo(person));
        add(grid);
    }

    // private HorizontalLayout createTaskInfo(Task Task) {
    // HorizontalLayout card = new HorizontalLayout();
    // card.addClassName("card");
    // card.setSpacing(false);
    // card.getThemeList().add("spacing-s");
    //
    // Image image = new Image();
    // image.setSrc(person.getImage());
    // VerticalLayout description = new VerticalLayout();
    // description.addClassName("description");
    // description.setSpacing(false);
    // description.setPadding(false);
    //
    // HorizontalLayout header = new HorizontalLayout();
    // header.addClassName("header");
    // header.setSpacing(false);
    // header.getThemeList().add("spacing-s");
    //
    // Span name = new Span(person.getName());
    // name.addClassName("name");
    // Span date = new Span(person.getDate());
    // date.addClassName("date");
    // header.add(name, date);
    //
    // Span post = new Span(person.getPost());
    // post.addClassName("post");
    //
    // HorizontalLayout actions = new HorizontalLayout();
    // actions.addClassName("actions");
    // actions.setSpacing(false);
    // actions.getThemeList().add("spacing-s");
    //
    // Icon likeIcon = VaadinIcon.HEART.create();
    // likeIcon.addClassName("icon");
    // Span likes = new Span(person.getLikes());
    // likes.addClassName("likes");
    // Icon commentIcon = VaadinIcon.COMMENT.create();
    // commentIcon.addClassName("icon");
    // Span comments = new Span(person.getComments());
    // comments.addClassName("comments");
    // Icon shareIcon = VaadinIcon.CONNECT.create();
    // shareIcon.addClassName("icon");
    // Span shares = new Span(person.getShares());
    // shares.addClassName("shares");
    //
    // actions.add(likeIcon, likes, commentIcon, comments, shareIcon, shares);
    //
    // description.add(header, post, actions);
    // card.add(image, description);
    // return card;
    // }
    //
    // //
    // public void afterNavigation(AfterNavigationEvent event) {
    //
    // // Set some data when this view is displayed.
    // List<Person> persons = Arrays.asList( //
    // createPerson("https://randomuser.me/api/portraits/men/42.jpg", "John Smith", "May 8",
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/women/42.jpg", "Abagail Libbie", "May 3",
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/men/24.jpg", "Alberto Raya", "May 3",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/women/24.jpg", "Emmy Elsner", "Apr 22",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/men/76.jpg", "Alf Huncoot", "Apr 21",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/women/76.jpg", "Lidmila Vilensky", "Apr 17",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/men/94.jpg", "Jarrett Cawsey", "Apr 17",
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/women/94.jpg", "Tania Perfilyeva", "Mar 8",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/men/16.jpg", "Ivan Polo", "Mar 5",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/women/16.jpg", "Emelda Scandroot", "Mar 5",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/men/67.jpg", "Marcos Sá", "Mar 4",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20"),
    // createPerson("https://randomuser.me/api/portraits/women/67.jpg", "Jacqueline Asong", "Mar 2",
    //
    // "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form
    // of a document without relying on meaningful content (also called greeking).",
    // "1K", "500", "20")
    //
    // );
    //
    // grid.setItems(persons);
    // }
    //
    // private static Person createPerson(String image, String name, String date, String post, String likes,
    // String comments, String shares) {
    // Person p = new Person();
    // p.setImage(image);
    // p.setName(name);
    // p.setDate(date);
    // p.setPost(post);
    // p.setLikes(likes);
    // p.setComments(comments);
    // p.setShares(shares);
    //
    // return p;
    // }

    private User getUserByTask(@NonNull final Task task) {
        final List<User> users = userCrudService.getRepository().findAll();
        final Optional<User> user = users.stream()
                .filter(x -> x.getUsername().equalsIgnoreCase(task.getAssignedPerformer()))
                .findFirst();
        if (user.isPresent()) {
            return user.get();
        } else {
            final User blank = new User();
            blank.setUsername("unassigned");
            blank.setName("unassigned");
            return blank;
        }
    }

}
