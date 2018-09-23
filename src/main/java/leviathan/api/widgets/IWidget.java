package leviathan.api.widgets;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import leviathan.api.events.EventDestination;
import leviathan.api.events.EventKey;
import leviathan.api.events.FocusEvent;
import leviathan.api.events.IEventListener;
import leviathan.api.events.IEventSystem;
import leviathan.api.events.JEGEvent;
import leviathan.api.geometry.ITransformProvider;

public interface IWidget extends ITransformProvider, ITooltipProvider, ILayout, IEventSystem {
	/**
	 * Draws the element and his children.
	 */
	void draw();

	void updateClient();

	void onCreation();

	void onDeletion();

	/**
	 * @return True of this element and its parent are visible.
	 */
	boolean isRecursivelyVisible();

	/**
	 * @return True of this element is currently visible.
	 */
	boolean isVisible();

	void setVisible(boolean visible);

	/**
	 * Sets the visible flag of this widget to true.
	 */
	default void show(){
		setVisible(true);
	}

	/**
	 * Sets the visible flag of this widget to false.
	 */
	default void hide(){
		setVisible(false);
	}

	void setName(String name);

	String getName();

	void onParentNameChange(String oldName, String newName);

	/**
	 * The most elements are enabled by default. Only a few elements are disabled at a certain time like buttons.
	 *
	 * @return True if this element and its parent are enabled.
	 */
	boolean isRecursivelyEnabled();

	/**
	 * @return True if this element is enabled.
	 */
	boolean isEnabled();

	void setEnabled(boolean enabled);

	Optional<IContainer> getContainer();

	void setContainer(@Nullable IContainer container);

	Optional<IWindow> getContainingWindow();

	void actOnWindow(Consumer<IWindow> windowConsumer);

	/* Events */
	default void dispatchEvent(JEGEvent event) {
		dispatchEvent(event, EventDestination.ALL);
	}

	default void dispatchEvent(JEGEvent event, EventDestination destination){
		destination.sendEvent(this, event);
	}

	default void dispatchEvent(JEGEvent event, Collection<IWidget> widgets){
		dispatchEvent(event, widgets, EventDestination.SINGLE);
	}

	default void dispatchEvent(JEGEvent event, Collection<IWidget> widgets, EventDestination destination){
		for (IWidget widget : widgets) {
			destination.sendEvent(widget, event);
		}
	}

	default <E extends JEGEvent> void addListener(EventKey<E> key, IEventListener<E> listener) {
		getEventSystem().addListener(key, listener);
	}

	default boolean hasListener(EventKey key) {
		return getEventSystem().hasListener(key);
	}

	default <E extends JEGEvent> void removeListener(EventKey<E> key, IEventListener<E> listener) {
		getEventSystem().removeListener(key, listener);
	}

	default void receiveEvent(JEGEvent event) {
		getEventSystem().receiveEvent(event);
	}

	IEventSystem getEventSystem();

	/* Screen State */
	default boolean canFocus(){
		return hasListener(FocusEvent.GAIN) || hasListener(FocusEvent.LOSE);
	}

	default boolean isFocused() {
		return getContainingWindow()
			.map(window -> window.getScreen().isFocused(this))
			.orElse(false);
	}

	default boolean isMouseOver() {
		return getContainingWindow()
			.map(window -> window.getScreen().isMouseOver(this))
			.orElse(false);
	}

	default boolean isDragged() {
		return getContainingWindow()
			.map(window -> window.getScreen().isDragged(this))
			.orElse(false);
	}
}
