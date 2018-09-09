package leviathan.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import leviathan.api.style.IElementContainer;
import leviathan.api.style.IGuiElement;

public class ElementContainer extends GuiElement implements IElementContainer {

	private final Map<String, IGuiElement> elements = new HashMap<>();

	public ElementContainer(String name) {
		super(name);
	}

	public void addElement(String name) {
		elements.put(name, new GuiElement(name));
	}

	@Override
	public IGuiElement getElement(String name) {
		return elements.get(name);
	}

	@Override
	public void ifPresent(String name, Consumer<IGuiElement> consumer) {
		IGuiElement elementStyle = getElement(name);
		if (elementStyle != null) {
			consumer.accept(elementStyle);
		}
	}
}
