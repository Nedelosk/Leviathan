package leviathan.api.gui;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.ILayoutManager;
import leviathan.api.gui.style.ITextStyle;
import leviathan.api.render.DrawMode;
import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;


@SideOnly(Side.CLIENT)
public interface IWidgetContainer extends IWidget, Iterable<IWidget> {
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

	@Nullable
	ILayoutManager getLayout();

	IWidgetContainer setLayout(ILayoutManager layoutManager);

	/**
	 * Computes all information needed for drawing and calls {@link #invalidate()}
	 * on child elementBuilders which has been changed. Don't call this method directly, it's for
	 * internal usage only, instead you should use {@link #validate()}.
	 */
	void doLayout();


	Collection<IWidget> calculateHoverElements(Predicate<IWidget> filter, boolean onlyFirst);

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

	default IWidgetContainer vertical(int width) {
		return vertical(0, 0, width);
	}

	default IWidgetContainer vertical(int width, int gap) {
		return vertical(0, 0, width, gap);
	}

	default IWidgetContainer vertical(int xPos, int yPos, int width){
		return vertical(xPos, yPos, width, 0);
	}

	IWidgetContainer vertical(int xPos, int yPos, int width, int gap);

	default IWidgetContainer horizontal(int xPos, int yPos, int height){
		return horizontal(xPos, yPos, height, 0);
	}

	IWidgetContainer horizontal(int xPos, int yPos, int height, int gap);

	default IWidgetContainer horizontal(int height) {
		return horizontal(0, 0, height);
	}

	default IWidgetContainer horizontal(int height, int gap) {
		return horizontal(0, 0, height, gap);
	}

	IWidgetContainer pane(int xPos, int yPos, int width, int height);

	default IWidgetContainer pane(int width, int height) {
		return pane(0, 0, width, height);
	}

	IWidgetContainer container(int xPos, int yPos, int width, int height);

	default IWidgetContainer container(int width, int height){
		return container(0, 0, width, height);
	}

	IWidgetLayoutHelper layoutHelper(IWidgetLayoutHelper.ContainerFactory layoutFactory, int width, int height);
}
