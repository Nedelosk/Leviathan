package leviathan.api;

import leviathan.api.gui.IWidget;

public interface IGuiElementRegistry {

	void registerElement(String id, Class<? extends IWidget> elementClass);

	Class<? extends IWidget> getElementClass(String id);
}
