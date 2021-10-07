package com.rubbers.team.views.list;

import com.rubbers.team.data.entity.item.Item;
import com.rubbers.team.data.entity.item.ItemStatus;
import com.rubbers.team.data.service.impl.ItemCrudService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Slf4j
public class ItemForm extends FormLayout {
	Binder<Item> binder = new BeanValidationBinder<>(Item.class);
	ItemCrudService itemCrudService;
	GridListDataView<Item> gridListDataView;
	private Item item;

	TextField descriptionArea = new TextField("Description");
	IntegerField countField = new IntegerField("Items count");
	ComboBox<ItemStatus> statusBox = new ComboBox<>("Status");
	TextField serialField = new TextField("Serial number");
	DatePicker datePicker = new DatePicker("Last update");
	TextField locationField = new TextField("Object location");
	TextField issueField = new TextField("Issue");
	Button save;
	Button delete;
	Button close;


	public ItemForm(ItemCrudService itemCrudService, GridListDataView<Item> gridListDataView) {
		addClassName("item-form");

		this.itemCrudService = itemCrudService;
		this.gridListDataView = gridListDataView;

//		binder.bindInstanceFields(this);
		statusBox.setItems(ItemStatus.values());
		statusBox.setValue(ItemStatus.ON_BUY);
		binder.forField(statusBox).bind(Item::getItemStatus, Item::setItemStatus);

		countField.setValue(1);
		countField.setMin(1);
		countField.setHasControls(true);
		binder.forField(countField).bind(Item::getItemCount, Item::setItemCount);

		datePicker.setValue(LocalDate.now());
		binder.forField(datePicker).bind(Item::getItemLastUpdate, Item::setItemLastUpdate);
		binder.forField(locationField).bind(Item::getItemLocation, Item::setItemLocation);
//		binder.forField(issueField).bind(Item::getItemIssue, Item::setItemIssue);

		add(descriptionArea,
				countField,
				statusBox,
				serialField,
				datePicker,
				locationField,
				createButtonsLayout());
	}

	public void setItem(Item item){
		this.item = item;
		binder.readBean(item);
	}



	private HorizontalLayout createButtonsLayout() {
		save = new Button("Save", buttonClickEvent -> validateAndSave());
		delete = new Button("Delete", buttonClickEvent -> fireEvent(new DeleteEvent(this, item)));
		close = new Button("Cancel", buttonClickEvent -> closeEditor());


		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);

		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
		return new HorizontalLayout(save, delete, close);
	}

	private void validateAndSave() {
		final Item clearItem = Item.builder().build();
		try {
			binder.writeBean(clearItem);
			itemCrudService.getRepository().save(clearItem);
			gridListDataView.addItem(clearItem);
			gridListDataView.refreshItem(clearItem);
			fireEvent(new SaveEvent(this, item));
			final Notification notification = new Notification(
					"Successfully updated item with id " + clearItem.getItemId(),
					3000, Notification.Position.BOTTOM_END);
			notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			notification.open();
		} catch (ValidationException validationException) {
			final Notification notification = new Notification(
					"Validation data error: " + validationException.getMessage(),
					3000, Notification.Position.BOTTOM_END);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			notification.open();
		} catch (Exception e) {
			log.error("Unable to update item in db", e);
			e.printStackTrace();
			final Notification notification = new Notification(
					"Error has occurred: " + e.getMessage() + ". Please contact IT-administrator",
					3000, Notification.Position.BOTTOM_END);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			notification.open();
		}
	}
	// Events
	public static abstract class ItemFormEvent extends ComponentEvent<ItemForm> {
		private Item item;

		protected ItemFormEvent(ItemForm source, Item item) {
			super(source, false);
			this.item = item;
		}

		public Item getItem() {
			return item;
		}
	}
	private void closeEditor() {
		setItem(null);
		setVisible(false);
		removeClassName("editing");
		fireEvent(new CloseEvent(this));
	}

	public static class SaveEvent extends ItemFormEvent {
		SaveEvent(ItemForm source, Item item) {
			super(source, item);
		}
	}

	public static class DeleteEvent extends ItemFormEvent {
		DeleteEvent(ItemForm source, Item item) {
			super(source, item);
		}

	}

	public static class CloseEvent extends ItemFormEvent {
		CloseEvent(ItemForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
																  ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}