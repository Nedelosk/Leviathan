package leviathan.api.gui;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The window element is the root element of the containment hierarchy. Its the only element the gui interacts with
 * directly.
 */
@SideOnly(Side.CLIENT)
public interface IWindowWidget extends IWidgetContainer {
	/**
	 * Returns the mouse position.
	 */
	int getMouseX();

	/**
	 * Returns the mouse position.
	 */
	int getMouseY();

	/**
	 * Returns the mouse position relative to the given element.
	 */
	int getRelativeMouseX(@Nullable IWidget element);

	/**
	 * Returns the mouse position relative to the given element.
	 */
	int getRelativeMouseY(@Nullable IWidget element);

	/**
	 * Returns the current screen width.
	 */
	int getScreenWidth();

	/**
	 * Returns the current screen height.
	 */
	int getScreenHeight();

	/**
	 * Returns the current gui height.
	 */
	int getGuiHeight();

	/**
	 * Returns the current gui width.
	 */
	int getGuiWidth();

	int getGuiLeft();

	int getGuiTop();

	GuiScreen getGui();

	TextureManager getTextureManager();

	FontRenderer getFontRenderer();

	@Nullable
	IWidget getMousedOverElement();

	@Nullable
	IWidget getDraggedElement();

	@Nullable
	IWidget getFocusedElement();

	void actionOnHovered(Predicate<IWidget> filter, Consumer<IWidget> action, boolean onlyFirst);

	boolean isMouseOver(IWidget element);

	boolean isDragged(IWidget element);

	boolean isFocused(IWidget element);

	Optional<IWidget> getWidget(String name);

	IWidget getWidgetOrEmpty(String name);
}
