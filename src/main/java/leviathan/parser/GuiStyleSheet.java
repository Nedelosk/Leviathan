package leviathan.parser;

import leviathan.api.style.IElementContainer;
import leviathan.api.style.IGuiStyleSheet;

public class GuiStyleSheet implements IGuiStyleSheet {
	private final IElementContainer window;

	public GuiStyleSheet(IElementContainer window) {
		this.window = window;
	}

	@Override
	public IElementContainer getWindow() {
		return window;
	}
}
