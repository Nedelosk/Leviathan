package leviathan.api.events;


import leviathan.api.widgets.IWidget;

public class KeyEvent extends JEGEvent {
	public static final EventKey<KeyEvent> KEY_DOWN = new EventKey<>("key_down", KeyEvent.class);
	public static final EventKey<KeyEvent> KEY_PRESS = new EventKey<>("key_press", KeyEvent.class);
	public static final EventKey<KeyEvent> KEY_UP = new EventKey<>("key_up", KeyEvent.class);

	private final char character;
	private final int key;

	public KeyEvent(IWidget origin, EventKey eventKey, char character, int key) {
		super(origin, eventKey);
		this.character = character;
		this.key = key;
	}

	public char getCharacter() {
		return this.character;
	}

	public int getKey() {
		return this.key;
	}
}
