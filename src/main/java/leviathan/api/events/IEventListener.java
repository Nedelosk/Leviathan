package leviathan.api.events;

public interface IEventListener<E extends JEGEvent> {

	void onAction(E event);
}
