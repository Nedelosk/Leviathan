package leviathan.api.events;

import leviathan.api.widgets.IWidget;


public class TextEditEvent extends ValueChangedEvent<String> {
	public static final EventKey<TextEditEvent> FINISH = new EventKey<>("edit_finished", TextEditEvent.class);

	public TextEditEvent(IWidget origin, String newValue, String oldValue) {
		super(origin, FINISH, newValue, oldValue);
	}

	public static class Finish extends TextEditEvent {
		public Finish(IWidget origin, String newValue, String oldValue) {
			super(origin, newValue, oldValue);
		}
	}
}
