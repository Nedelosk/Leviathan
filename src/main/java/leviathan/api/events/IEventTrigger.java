package leviathan.api.events;

public interface IEventTrigger<E extends JEGEvent> {
	void addListener(IEventListener<E> listener);

	void removeListener(IEventListener<E> listener);

	boolean hasListeners();

	void receiveEvent(E event);
}
