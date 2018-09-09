package leviathan.gui.events;

import javax.annotation.Nullable;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.events.EventKey;
import leviathan.api.gui.events.JEGEvent;

public class EventValueChanged<V> extends JEGEvent {
	public static final EventKey<EventValueChanged> VALUE_CHANGED = new EventKey<>("value_change", EventValueChanged.class);

	@Nullable
	private final V value;

	public EventValueChanged(IWidget origin, V value) {
		super(origin, VALUE_CHANGED);
		this.value = value;
	}

	@Nullable
	public V getValue() {
		return value;
	}
}
