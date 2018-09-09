package leviathan.api.gui.events;

public interface IEventSystem {

	<E extends JEGEvent> void addListener(EventKey<E> key, IEventListener<E> listener);

	boolean hasListener(EventKey key);

	<E extends JEGEvent> void removeListener(EventKey<E> key, IEventListener<E> listener);

	void receiveEvent(JEGEvent event);
}
