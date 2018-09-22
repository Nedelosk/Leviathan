package leviathan.gui.events;

import java.util.HashMap;
import java.util.Map;

import leviathan.api.events.EventKey;
import leviathan.api.events.IEventListener;
import leviathan.api.events.IEventSystem;
import leviathan.api.events.IEventTrigger;
import leviathan.api.events.JEGEvent;

public class EventSystem implements IEventSystem {
	private final Map<EventKey, IEventTrigger> triggers = new HashMap<>();

	@Override
	public <E extends JEGEvent> void addListener(EventKey<E> key, IEventListener<E> listener) {
		triggers.computeIfAbsent(key, k -> new EventTrigger<>(k)).addListener(listener);
	}

	@Override
	public boolean hasListener(EventKey key) {
		return triggers.containsKey(key);
	}

	@Override
	public <E extends JEGEvent> void removeListener(EventKey<E> key, IEventListener<E> listener) {
		IEventTrigger trigger = triggers.get(key);
		if (trigger == null) {
			return;
		}
		trigger.removeListener(listener);
		if (!trigger.hasListeners()) {
			triggers.remove(key);
		}
	}

	@Override
	public void receiveEvent(JEGEvent event) {
		EventKey key = event.getEventKey();
		IEventTrigger trigger = triggers.get(key);
		if (trigger == null) {
			return;
		}
		trigger.receiveEvent(event);
	}
}
