package leviathan.api.gui;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.style.ITextStyle;
import leviathan.api.render.DrawMode;
import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;


@SideOnly(Side.CLIENT)
public interface IWidgetGroup extends IWidget {
	/**
	 * Adds a element to this layout.
	 */
	<E extends IWidget> E add(E element);

	/**
	 * Removes a element from this layout.
	 */
	<E extends IWidget> E remove(E element);

	default IWidget add(IWidget... elements) {
		for (IWidget element : elements) {
			add(element);
		}
		return this;
	}

	default IWidget remove(IWidget... elements) {
		for (IWidget element : elements) {
			remove(element);
		}
		return this;
	}

	default IWidget add(Collection<IWidget> elements) {
		elements.forEach(this::add);
		return this;
	}

	default IWidget remove(Collection<IWidget> elements) {
		elements.forEach(this::remove);
		return this;
	}


	void clear();

	@Nullable
	IWidget getLastElement();

	/**
	 * @return All elements that this layout contains.
	 */
	List<IWidget> getElements();

	Collection<IWidget> calculateHoverElements(Predicate<IWidget> filter);

	@Nullable
	IWidget calculateHoverElement(Predicate<IWidget> filter);

	@Nullable
	IWidget calculateMousedOverElement();

	Deque<IWidget> calculateMousedOverElements();

	Collection<IWidget> getQueuedElements(final IWidget element);

	IItemWidget item(int xPos, int yPos, ItemStack itemStack);

	default IItemWidget item(ItemStack itemStack) {
		return item(0, 0, itemStack);
	}

	/**
	 * Adds a single line of text.
	 */
	ILabelWidget label(String text);

	ILabelWidget label(String text, ITextStyle style);

	ILabelWidget label(String text, WidgetAlignment align);

	ILabelWidget label(String text, WidgetAlignment align, ITextStyle textStyle);

	ILabelWidget label(String text, int width, int height, WidgetAlignment align, ITextStyle textStyle);

	ILabelWidget label(String text, int x, int y, int width, int height, WidgetAlignment align, ITextStyle textStyle);

	/**
	 * Adds a text element that splits the text with wordwrap.
	 */
	ITextWidget splitText(String text, int width);

	ITextWidget splitText(String text, int width, ITextStyle textStyle);

	ITextWidget splitText(String text, int width, WidgetAlignment align, ITextStyle textStyle);

	ITextWidget splitText(String text, int x, int y, int width, WidgetAlignment align, ITextStyle textStyle);

	ITextureWidget drawable(ISprite texture);

	ITextureWidget drawable(DrawMode mode, ISprite texture);

	ITextureWidget drawable(int x, int y, ISprite texture);

	ITextureWidget drawable(int x, int y, DrawMode mode, ISprite texture);

	ITextureWidget drawable(int x, int y, int width, int height, DrawMode mode, ISprite texture);

	ITextureWidget drawable(IDrawable drawable);

	ITextureWidget drawable(int x, int y, IDrawable drawable);

	ITextureWidget drawable(int x, int y, int width, int height, IDrawable drawable);

	default IWidgetLayout vertical(int width) {
		return vertical(0, 0, width);
	}

	IWidgetLayout vertical(int xPos, int yPos, int width);

	IWidgetLayout horizontal(int xPos, int yPos, int height);

	default IWidgetLayout horizontal(int height) {
		return horizontal(0, 0, height);
	}

	IWidgetGroup pane(int xPos, int yPos, int width, int height);

	default IWidgetGroup pane(int width, int height) {
		return pane(0, 0, width, height);
	}

	IWidgetLayoutHelper layoutHelper(IWidgetLayoutHelper.LayoutFactory layoutFactory, int width, int height);
}
