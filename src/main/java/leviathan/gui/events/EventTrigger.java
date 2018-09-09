package leviathan.gui.events;

import java.util.ArrayList;
import java.util.List;

import leviathan.api.gui.events.EventKey;
import leviathan.api.gui.events.IEventListener;
import leviathan.api.gui.events.IEventTrigger;
import leviathan.api.gui.events.JEGEvent;

public class EventTrigger<E extends JEGEvent> implements IEventTrigger<E> {

	private final EventKey<E> eventKey;
	private final List<IEventListener<E>> listeners;

	public EventTrigger(EventKey<E> eventKey) {
		this.eventKey = eventKey;
		this.listeners = new ArrayList<>();
	}

	@Override
	public void addListener(IEventListener<E> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(IEventListener<E> listener) {
		listeners.remove(listener);
	}

	@Override
	public boolean hasListeners() {
		return !listeners.isEmpty();
	}

	@Override
	public void receiveEvent(E event) {
		for (IEventListener<E> listener : listeners) {
			listener.onAction(event);
		}
	}
}
