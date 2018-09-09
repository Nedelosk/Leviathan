package leviathan.api.style;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public interface IElementContainer extends IGuiElement {
	@Nullable
	IGuiElement getElement(String name);

	void ifPresent(String name, Consumer<IGuiElement> consumer);
}
