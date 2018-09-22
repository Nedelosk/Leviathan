package leviathan.api.events;

import leviathan.api.widgets.IWidget;

public class FocusEvent extends JEGEvent {
	public static final EventKey<FocusEvent> GAIN = new EventKey("focus_gain", FocusEvent.class);
	public static final EventKey<FocusEvent> LOSE = new EventKey("focus_lose", FocusEvent.class);

	public FocusEvent(IWidget source, EventKey key) {
		super(source, key);
	}
}
