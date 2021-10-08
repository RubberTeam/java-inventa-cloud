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
package com.rubbers.team.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.rubbers.team.data.entity.user.User;
import com.rubbers.team.security.AuthenticatedUser;
import com.rubbers.team.views.cardlist.TasksLikeTwitterListView;
import com.rubbers.team.views.list.ListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("Main")
public class MainLayout extends AppLayout {

    private final AccessAnnotationChecker accessChecker;
    private final AuthenticatedUser authenticatedUser;
    private H1 viewTitle;

    public MainLayout(final AuthenticatedUser authenticatedUser, final AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        final DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        final Header header = new Header(toggle, viewTitle);
        header.addClassNames(
                "bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center", "w-full");
        return header;
    }

    private Component createDrawerContent() {
        final H2 appName = new H2("Inventa");
        appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-m");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(
                appName,
                createNavigation(),
                createFooter());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
        return section;
    }

    private Nav createNavigation() {
        final Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        final H3 views = new H3("Views");
        views.addClassNames("flex", "h-m", "items-center", "mx-m", "my-0", "text-s", "text-tertiary");
        views.setId("views");

        // Wrap the links in a list; improves accessibility
        final UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0");
        nav.add(list);
        for (RouterLink link : createLinks()) {
            list.add(new ListItem(link));
        }
        return nav;
    }

    private List<RouterLink> createLinks() {
        final MenuItemInfo[] menuItems = new MenuItemInfo[] {
                new MenuItemInfo("Items", "la la-bank", ListView.class),
                new MenuItemInfo("Tasks", "la la-barcode", TasksLikeTwitterListView.class)
        };

        final List<RouterLink> links = new ArrayList<>();
        for (final MenuItemInfo menuItemInfo : menuItems) {
            if (accessChecker.hasAccess(menuItemInfo.getView())) {
                links.add(createLink(menuItemInfo));
            }
        }
        return links;
    }

    private static RouterLink createLink(MenuItemInfo menuItemInfo) {
        final RouterLink link = new RouterLink();
        link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
        link.setRoute(menuItemInfo.getView());

        final Span icon = new Span();
        icon.addClassNames("me-s", "text-l");
        if (!menuItemInfo.getIconClass().isEmpty()) {
            icon.addClassNames(menuItemInfo.getIconClass());
        }

        final Span text = new Span(menuItemInfo.getText());
        text.addClassNames("font-medium", "text-s");

        link.add(icon, text);
        return link;
    }

    private Footer createFooter() {
        final Footer layout = new Footer();
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

        final Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            final User user = maybeUser.get();

            final Avatar avatar = new Avatar(user.getName(), user.getProfilePictureUrl());
            avatar.addClassNames("me-xs");

            final ContextMenu userMenu = new ContextMenu(avatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Сменить тему", e -> {
                ThemeList themeList = UI.getCurrent().getElement().getThemeList();
                if (themeList.contains(Lumo.DARK)) {
                    themeList.remove(Lumo.DARK);
                } else {
                    themeList.add(Lumo.DARK);
                }
            });
            userMenu.addItem("Выйти", e -> authenticatedUser.logout());

            final Span name = new Span(user.getName());
            name.addClassNames("font-medium", "text-s", "text-secondary");

            layout.add(avatar, name);
        } else {
            final Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
