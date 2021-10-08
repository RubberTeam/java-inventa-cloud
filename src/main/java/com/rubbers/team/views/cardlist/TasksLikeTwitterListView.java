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

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rubbers.team.data.Role;
import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.entity.task.Task;
import com.rubbers.team.data.entity.task.TaskStatus;
import com.rubbers.team.data.entity.user.User;
import com.rubbers.team.data.service.impl.IssueCrudService;
import com.rubbers.team.data.service.impl.ItemCrudService;
import com.rubbers.team.data.service.impl.TaskCrudService;
import com.rubbers.team.data.service.impl.UserCrudService;
import com.rubbers.team.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.NonNull;
import lombok.val;

@PermitAll
@PageTitle("Tasks")
@Route(value = "task-list", layout = MainLayout.class)
public class TasksLikeTwitterListView extends Div {

    private final ItemCrudService itemCrudService;
    private final UserCrudService userCrudService;
    private final IssueCrudService issueCrudService;
    private final TaskCrudService taskCrudService;
    private final PasswordEncoder passwordEncoder;

    private final Grid<Task> grid = new Grid<>();

    public TasksLikeTwitterListView(@Autowired final ItemCrudService itemCrudService,
            @Autowired final UserCrudService userCrudService,
            @Autowired final IssueCrudService issueCrudService,
            @Autowired final TaskCrudService taskCrudService,
            @Autowired final PasswordEncoder passwordEncoder) {
        this.itemCrudService = itemCrudService;
        this.userCrudService = userCrudService;
        this.taskCrudService = taskCrudService;
        this.issueCrudService = issueCrudService;
        this.passwordEncoder = passwordEncoder;

        addClassName("card-list-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(task -> createTaskInfo(task));
        grid.addComponentColumn(task -> createProgressBar(task));
        grid.setItems(taskCrudService.getRepository().findAll());
        add(grid);
    }

    private HorizontalLayout createTaskInfo(@NonNull final Task task) {
        final HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        final User user = getUserByTask(task);
        final Image image = new Image();
        image.setSrc(user.getProfilePictureUrl());

        final VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        final HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        final Span name = new Span("Исполнитель: " + user.getName());
        name.addClassName("name");

        final Span email = new Span(user.getEmail());
        name.addClassName("email");

        final Button justStatus = new Button(task.getTaskStatus().getStatusName());
        justStatus.setVisible(true);
        if (TaskStatus.DONE.equals(task.getTaskStatus())) {
            justStatus.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        }
        if (TaskStatus.ISSUE.equals(task.getTaskStatus())) {
            justStatus.addThemeVariants(ButtonVariant.LUMO_ERROR);
        }

        final Span date = new Span(task.getCreationDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        date.addClassName("date");
        header.add(name, email, date, justStatus);

        final Span post = new Span("Задача по " + task.getOrderDocument());
        post.addClassName("post");

        final HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        final Icon taskIcon = VaadinIcon.ARCHIVES.create();
        taskIcon.addClassName("icon");
        final Span tasks = new Span(String.valueOf(task.getItems().size()));
        tasks.addClassName("taskCount");

        final Icon issueIcon = VaadinIcon.AMBULANCE.create();
        issueIcon.addClassName("icon");
        Span issues = new Span(String.valueOf(task.getIssues().size()));
        issues.addClassName("issueCount");
        if (TaskStatus.ISSUE.equals(task.getTaskStatus())) {
            issues = new Span("1");
        }

        actions.add(taskIcon, tasks, issueIcon, issues);
        description.add(header, post, actions);
        card.add(image, description);

        return card;
    }

    public VerticalLayout createProgressBar(final Task task) {
        final VerticalLayout card = new VerticalLayout();
        card.addClassName("card");

        final ProgressBar progressBar = new ProgressBar();

        final int itemsInProgress = task.getItems()
                .stream()
                .filter(x -> !x.getTaskCurrentlyInventoried())
                .collect(Collectors.toList())
                .size();
        final int allItemsCount = task.getItems().size();
        Div progressBarLabel = new Div();

        final double progressValue = allItemsCount == 0 ? 1.0 : itemsInProgress * 1.0 / allItemsCount;
        progressBarLabel.setText("Выполнено на " + itemsInProgress + " из " + allItemsCount);

        if (TaskStatus.ISSUE.equals(task.getTaskStatus())) {
            progressBar.setValue(progressValue);
            progressBar.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
        }
        if (TaskStatus.ASSIGNED.equals(task.getTaskStatus())
                || TaskStatus.SCHEDULED.equals(task.getTaskStatus())
                || TaskStatus.CREATED.equals(task.getTaskStatus())) {
            progressBar.setValue(progressValue);
            progressBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);
        }
        if (TaskStatus.DONE.equals(task.getTaskStatus())) {
            progressBar.setValue(1.0);
            progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
        }

        if (TaskStatus.IN_PROGRESS.equals(task.getTaskStatus())) {
            progressBar.setValue(progressValue);
            progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
        }

        card.add(progressBarLabel, progressBar);
        return card;
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

    /**
     * Метод для получения пользователя для красивого отображения
     *
     * @param task таск
     * @return User или
     */
    private User getUserByTask(@NonNull final Task task) {
        final List<User> users = userCrudService.getRepository().findAll();
        final Optional<User> user = users.stream()
                .filter(x -> x.getUsername().equalsIgnoreCase(task.getAssignedPerformer()))
                .findFirst();
        return user.orElse(getStubUser());
    }

    private User getStubUser() {
        final User user = new User();
        user.setName("John Normal");
        user.setUsername("user");
        user.setEmail("John.Normal@sberbank.ru");
        user.setHashedPassword(passwordEncoder.encode("user"));
        user.setProfilePictureUrl("https://randomuser.me/api/portraits/men/42.jpg");
        user.setRoles(Collections.singleton(Role.USER));
        return user;
    }

}
