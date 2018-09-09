package leviathan.parser;

import leviathan.api.style.IGuiElement;

public class GuiElement implements IGuiElement {
	public final String name;

	public GuiElement(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
