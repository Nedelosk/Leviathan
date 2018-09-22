package leviathan.api.events;

import java.util.Objects;

public class EventKey<E extends JEGEvent> {
	private final Class<? extends E> eventClass;
	private final String name;

	public EventKey(String name, Class<? extends E> eventClass) {
		this.name = name;
		this.eventClass = eventClass;
	}

	public String getName() {
		return name;
	}

	public Class<? extends E> getEventClass() {
		return eventClass;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EventKey<?> eventKey = (EventKey<?>) o;
		return Objects.equals(name, eventKey.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
