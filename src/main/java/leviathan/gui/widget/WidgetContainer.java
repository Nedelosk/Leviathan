package leviathan.gui.widget;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.ILayoutManager;
import leviathan.api.geometry.Region;
import leviathan.api.text.GuiConstants;
import leviathan.api.gui.IItemWidget;
import leviathan.api.gui.ILabelWidget;
import leviathan.api.gui.ITextWidget;
import leviathan.api.gui.ITextureWidget;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetContainer;
import leviathan.api.gui.IWidgetLayoutHelper;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.text.ITextStyle;
import leviathan.api.render.DrawMode;
import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;
import leviathan.gui.layouts.HorizontalLayout;
import leviathan.gui.layouts.PaneLayout;
import leviathan.gui.layouts.VerticalLayout;
import leviathan.gui.layouts.WidgetLayoutHelper;
import leviathan.utils.Drawable;


@SideOnly(Side.CLIENT)
public class WidgetContainer extends Widget implements IWidgetContainer {
	protected final List<IWidget> elements = new ArrayList<>();
	@Nullable
	protected ILayoutManager layoutManager;

	public WidgetContainer(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}

	public <E extends IWidget> E add(E element) {
		elements.add(element);
		element.setParent(this);
		invalidate();
		return element;
	}

	public <E extends IWidget> E remove(E element) {
		elements.remove(element);
		element.onDeletion();
		invalidate();
		return element;
	}

	public void clear() {
		for (IWidget element : new ArrayList<>(elements)) {
			remove(element);
		}
	}

	@Override
	public void onDeletion() {
		super.onDeletion();
		for (IWidget child : elements) {
			child.onDeletion();
		}
	}

	@Override
	protected void onRegionChange(Region oldRegion, Region newRegion) {
		super.onRegionChange(oldRegion, newRegion);
		for (IWidget widget : elements) {
			widget.onParentRegionChange(oldRegion, newRegion);
		}
	}

	@Override
	public List<IWidget> getElements() {
		return elements;
	}

	@Override
	public Iterator<IWidget> iterator() {
		return elements.iterator();
	}

	@Override
	public ILayoutManager getLayout() {
		return layoutManager;
	}

	@Override
	public IWidgetContainer setLayout(@Nullable ILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
		return this;
	}

	@Override
	public void doLayout() {
		if(layoutManager != null){
			layoutManager.layoutWidget(this);
		}
	}

	@Override
	protected void doValidate() {
		super.doValidate();
		doLayout();
		for(IWidget widget : elements){
			widget.validate();
		}
	}

	/*@Override
	public int getRelativeMouseX(@Nullable IWidget element) {
		Preconditions.checkNotNull(parent, "Failed to find the top level widget of the widget ('" + name + "'). ");
		int relativeX = parent.getRelativeMouseX(this);
		if (element == null) {
			return relativeX;
		}
		return relativeX - element.getX();
	}

	@Override
	public int getRelativeMouseY(@Nullable IWidget element) {
		Preconditions.checkNotNull(parent, "Failed to find the top level widget of the widget ('" + name + "'). ");
		int relativeY = parent.getRelativeMouseY(this);
		if (element == null) {
			return relativeY;
		}
		return relativeY - element.getY();
	}

	@Override
	public Point getRelativeMousePosition(@Nullable IWidget element) {
		Preconditions.checkNotNull(parent, "Failed to find the top level widget of the widget ('" + name + "'). ");
		Point relativePosition = parent.getRelativeMousePosition(this);
		if (element == null) {
			return relativePosition;
		}
		return relativePosition.subtract(element.getPosition());
	}*/

	public Collection<IWidget> calculateHoverElements(Predicate<IWidget> filter, boolean onlyFirst) {
		List<IWidget> widgets = new LinkedList<>();
		Deque<IWidget> queue = this.calculateMousedOverElements();
		while (!queue.isEmpty()) {
			IWidget element = queue.removeFirst();
			if (element.isEnabled() && element.isVisible() && filter.test(element)) {
				widgets.add(element);
				if(onlyFirst){
					break;
				}
			}
		}
		return widgets;
	}

	@Override
	@Nullable
	public IWidget calculateHoverElement(Predicate<IWidget> filter) {
		Deque<IWidget> queue = this.calculateMousedOverElements();
		while (!queue.isEmpty()) {
			IWidget element = queue.removeFirst();
			if (element.isEnabled() && element.isVisible() && filter.test(element)) {
				return element;
			}
		}
		return null;
	}

	@Nullable
	public IWidget calculateMousedOverElement() {
		Deque<IWidget> queue = this.calculateMousedOverElements();
		while (!queue.isEmpty()) {
			IWidget element = queue.removeFirst();
			if (element.isEnabled() && element.isVisible() && element.canMouseOver()) {
				return element;
			}
		}
		return null;
	}

	public Deque<IWidget> calculateMousedOverElements() {
		Deque<IWidget> list = new ArrayDeque<>();
		for (IWidget element : this.getQueuedElements(this)) {
			if (element.isMouseOver()) {
				list.addLast(element);
			}
		}
		return list;
	}

	public Collection<IWidget> getQueuedElements(final IWidget element) {
		if (!hasWindow()) {
			return Collections.emptyList();
		}
		IWindowWidget window = getWindow();
		List<IWidget> widgets = new ArrayList<>();
		if (element instanceof IWidgetContainer) {
			IWidgetContainer group = (IWidgetContainer) element;
			boolean addChildren = true;
			if (element.isCropped()) {
				int relativeMouseX = window.getRelativeMouseX(element);
				int relativeMouseY = window.getRelativeMouseY(element);
				IWidget cropRelative = !element.getCropElement().isEmpty() ? element.getCropElement() : this;
				int posX = cropRelative.getAbsoluteX() - element.getAbsoluteX();
				int posY = cropRelative.getAbsoluteY() - element.getAbsoluteY();
				Region cropRegion = element.getCroppedRegion();
				addChildren = relativeMouseX >= posX && relativeMouseY >= posY && relativeMouseX <= posX + cropRegion.getWidth() && relativeMouseY <= posY + cropRegion.getHeight();
			}
			if (addChildren) {
				ListIterator<IWidget> iterator = group.getElements().listIterator(group.getElements().size());
				while (iterator.hasPrevious()) {
					final IWidget child = iterator.previous();
					widgets.addAll(this.getQueuedElements(child));
				}
			}
		}
		widgets.add(element);
		return widgets;
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		int mX = mouseX - getX();
		int mY = mouseY - getY();
		elements.forEach(element -> element.draw(mX - element.getX(), mY - element.getY()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateClient() {
		if (!isVisible()) {
			return;
		}
		onUpdateClient();
		for (IWidget widget : getElements()) {
			widget.updateClient();
		}
	}

	@Nullable
	@Override
	public IWidget getLastElement() {
		return elements.isEmpty() ? null : elements.get(elements.size() - 1);
	}

	@Override
	public ITextureWidget drawable(ISprite texture) {
		return drawable(0, 0, texture);
	}

	@Override
	public ITextureWidget drawable(DrawMode mode, ISprite texture) {
		return drawable(0, 0, mode, texture);
	}

	@Override
	public ITextureWidget drawable(int x, int y, DrawMode mode, ISprite texture) {
		return drawable(x, y, texture.getWidth(), texture.getHeight(), mode, texture);
	}

	@Override
	public ITextureWidget drawable(int x, int y, int width, int height, DrawMode mode, ISprite texture) {
		return drawable(x, y, width, height, new Drawable(mode, texture));
	}

	@Override
	public ITextureWidget drawable(int x, int y, int width, int height, IDrawable drawable) {
		return add(new TextureWidget(x, y, width, height, drawable));
	}

	@Override
	public ITextureWidget drawable(int x, int y, ISprite texture) {
		return drawable(x, y, DrawMode.SIMPLE, texture);
	}

	@Override
	public ITextureWidget drawable(IDrawable drawable) {
		return drawable(0, 0, drawable);
	}

	@Override
	public ITextureWidget drawable(int x, int y, IDrawable drawable) {
		return drawable(x, y, drawable.getSprite().getWidth(), drawable.getSprite().getHeight(), drawable);
	}

	@Override
	public IItemWidget item(int xPos, int yPos, ItemStack itemStack) {
		IItemWidget element = new ItemWidget(xPos, yPos, itemStack);
		add(element);
		return element;
	}

	@Override
	public ILabelWidget label(String text) {
		return label(text, GuiConstants.DEFAULT_STYLE);
	}

	@Override
	public ILabelWidget label(String text, ITextStyle style) {
		return label(text, WidgetAlignment.TOP_LEFT, style);
	}

	@Override
	public ILabelWidget label(String text, WidgetAlignment align) {
		return label(text, align, GuiConstants.DEFAULT_STYLE);
	}

	@Override
	public ILabelWidget label(String text, WidgetAlignment align, ITextStyle textStyle) {
		return label(text, -1, 12, align, textStyle);
	}

	@Override
	public ILabelWidget label(String text, int width, int height, WidgetAlignment align, ITextStyle textStyle) {
		return label(text, 0, 0, width, height, align, textStyle);
	}

	@Override
	public ILabelWidget label(String text, int x, int y, int width, int height, WidgetAlignment align, ITextStyle textStyle) {
		return add(new LabelWidget(x, y, width, height, text, align, textStyle));
	}

	@Override
	public ITextWidget splitText(String text, int width) {
		return splitText(text, width, GuiConstants.DEFAULT_STYLE);
	}

	@Override
	public ITextWidget splitText(String text, int width, ITextStyle textStyle) {
		return splitText(text, width, WidgetAlignment.TOP_LEFT, textStyle);
	}

	@Override
	public ITextWidget splitText(String text, int width, WidgetAlignment align, ITextStyle textStyle) {
		return splitText(text, 0, 0, width, align, textStyle);
	}

	@Override
	public ITextWidget splitText(String text, int x, int y, int width, WidgetAlignment align, ITextStyle textStyle) {
		return add(new SplitTextWidget(x, y, width, text, align, textStyle));
	}

	@Override
	public IWidgetContainer container(int xPos, int yPos, int width, int height) {
		return add(new WidgetContainer(xPos, yPos, width, height));
	}

	@Override
	public IWidgetContainer vertical(int xPos, int yPos, int width, int gap) {
		return container(xPos, yPos, width, 0).setLayout(new VerticalLayout(gap));
	}

	@Override
	public IWidgetContainer horizontal(int xPos, int yPos, int height, int gap) {
		return container(xPos, yPos, 0, height).setLayout(new HorizontalLayout(gap));
	}

	@Override
	public IWidgetContainer pane(int xPos, int yPos, int width, int height) {
		return container(xPos, yPos, width, height);
	}

	@Override
	public IWidgetContainer pane(int width, int height) {
		return container(0, 0, width, height).setLayout(PaneLayout.INSTANCE);
	}

	@Override
	public WidgetLayoutHelper layoutHelper(IWidgetLayoutHelper.ContainerFactory layoutFactory, int width, int height) {
		return new WidgetLayoutHelper(layoutFactory, width, height, this);
	}
}
