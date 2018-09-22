package leviathan.api.events;


import leviathan.api.widgets.IWidget;

public class JEGEvent {
	private final IWidget source;
	private final EventKey key;

	public JEGEvent(IWidget source, EventKey key) {
		this.source = source;
		this.key = key;
	}

	public IWidget getSource() {
		return source;
	}

	public final boolean isSource(IWidget element) {
		return this.source == element;
	}

	public EventKey getEventKey() {
		return key;
	}
}
