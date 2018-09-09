package leviathan.api.gui.events;

import leviathan.api.gui.IWidget;

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
