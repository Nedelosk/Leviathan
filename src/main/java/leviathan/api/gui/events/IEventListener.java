package leviathan.api.gui.events;

public interface IEventListener<E extends JEGEvent> {

	void onAction(E event);
}
