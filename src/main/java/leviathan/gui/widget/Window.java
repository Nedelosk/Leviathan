package leviathan.gui.widget;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;

import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetGroup;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.gui.events.ElementEvent;
import leviathan.api.gui.events.EventDestination;
import leviathan.api.gui.events.FocusEvent;
import leviathan.api.gui.events.JEGEvent;
import leviathan.api.gui.events.MouseEvent;
import leviathan.gui.IGuiSizable;
import leviathan.gui.widget.layouts.WidgetGroup;
import org.lwjgl.input.Mouse;

/**
 * This element is the top parent.
 */
@SideOnly(Side.CLIENT)
public class Window<G extends GuiScreen & IGuiSizable> extends WidgetGroup implements IWindowWidget {
	private final Map<String, IWidget> widgets = new HashMap<>();
	protected final G gui;
	@Nullable
	private Minecraft mc = null;
	//The last x position of the mouse
	protected int mouseX = -1;
	//The last y position of the mouse
	protected int mouseY = -1;
	@Nullable
	private IWidget mousedOverElement;
	@Nullable
	private IWidget draggedElement;
	@Nullable
	private IWidget focusedElement;

	public Window(int width, int height, G gui) {
		super(0, 0, width, height);
		this.gui = gui;
		addListener(ElementEvent.DELETION, deletion -> {
			IWidget element = deletion.getSource();
			if (isMouseOver(element)) {
				setMousedOverElement(null);
			}
			if (isDragged(element)) {
				setDraggedElement(null);
			}
			if (isFocused(element)) {
				setFocusedElement(null);
			}
			if (element.getName() != null) {
				widgets.remove(element.getName());
			}
		});
		addListener(ElementEvent.CREATION, event -> {
			addElement(event.getSource());
		});
		addListener(MouseEvent.MOUSE_DOWN, event -> {
			this.setDraggedElement(calculateHoverElement(widget -> widget.hasListener(MouseEvent.MOUSE_DRAG_START) || widget.hasListener(MouseEvent.MOUSE_DRAG_MOVE) || widget.hasListener(MouseEvent.MOUSE_DRAG_END)), event.getButton());
			this.setFocusedElement(mousedOverElement);
		});
		addListener(MouseEvent.MOUSE_UP, event -> setDraggedElement(null));
		addListener(ElementEvent.RENAME, event -> {
			IWidget widget = event.getSource();
			String oldName = event.getOldName();
			String newName = event.getNewName();
			IWidget currentWidget = widgets.get(newName);
			if (currentWidget != null) {
				event.setCanceled(true);
				return;
			}
			widgets.remove(oldName);
			widgets.put(newName, widget);
		});
	}

	@Override
	public IWindowWidget getWindow() {
		return this;
	}

	@Override
	public Window<G> setParent(@Nullable IWidget parent) {
		return this;
	}

	@Override
	public boolean hasParent() {
		return false;
	}

	@Override
	public boolean hasWindow() {
		return true;
	}

	public void init(int guiLeft, int guiTop) {
		setLocation(guiLeft, guiTop);
	}

	/* Element Events */
	@Nullable
	public IWidget getDraggedElement() {
		return this.draggedElement;
	}

	public void setDraggedElement(@Nullable IWidget widget) {
		this.setDraggedElement(widget, -1);
	}

	public void setDraggedElement(@Nullable IWidget widget, int button) {
		if (this.draggedElement == widget) {
			return;
		}
		IWidget oldDragged = draggedElement;
		this.draggedElement = widget;
		if (oldDragged != null) {
			dispatchEvent(new MouseEvent(oldDragged, MouseEvent.MOUSE_DRAG_END, mouseX, mouseY, button));
		}
		if (this.draggedElement != null) {
			dispatchEvent(new MouseEvent(this.draggedElement, MouseEvent.MOUSE_DRAG_START, mouseX, mouseY, button));
		}
	}

	@Override
	@Nullable
	public IWidget getMousedOverElement() {
		return this.mousedOverElement;
	}

	public void setMousedOverElement(@Nullable IWidget widget) {
		if (this.mousedOverElement == widget) {
			return;
		}
		if (this.mousedOverElement != null) {
			dispatchEvent(new MouseEvent(this.mousedOverElement, MouseEvent.MOUSE_LEAVE, mouseX, mouseY, -1));
		}
		this.mousedOverElement = widget;
		if (this.mousedOverElement != null) {
			dispatchEvent(new MouseEvent(this.mousedOverElement, MouseEvent.MOUSE_ENTER, mouseX, mouseY, -1));
		}
	}

	@Nullable
	public IWidget getFocusedElement() {
		return this.focusedElement;
	}

	public void setFocusedElement(@Nullable IWidget widget) {
		IWidget newElement = widget;
		if (this.focusedElement == newElement) {
			return;
		}
		if (newElement != null && !newElement.canFocus()) {
			newElement = null;
		}
		if (this.focusedElement != null) {
			dispatchEvent(new FocusEvent(this.focusedElement, FocusEvent.LOSE));
		}
		this.focusedElement = newElement;
		if (this.focusedElement != null) {
			dispatchEvent(new FocusEvent(this.focusedElement, FocusEvent.GAIN));
		}
	}

	@Override
	public boolean isMouseOver(final IWidget element) {
		return this.getMousedOverElement() == element;
	}

	public boolean isDragged(final IWidget element) {
		return this.getDraggedElement() == element;
	}

	public boolean isFocused(final IWidget element) {
		return this.getFocusedElement() == element;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateClient() {
		if (!isVisible()) {
			return;
		}
		updateWindow();
		onUpdateClient();
		for (IWidget widget : getElements()) {
			widget.updateClient();
		}
	}

	protected void updateWindow() {
		this.setMousedOverElement(this.calculateMousedOverElement());
		if (this.getFocusedElement() != null && (!this.getFocusedElement().isVisible() || !this.getFocusedElement().isEnabled())) {
			this.setFocusedElement(null);
		}
		if (!Mouse.isButtonDown(0)) {
			if (this.draggedElement != null) {
				this.setDraggedElement(null);
			}
		}
	}

	@Override
	public void actionOnHovered(Predicate<IWidget> filter, Consumer<IWidget> action) {
		calculateHoverElements(filter).forEach(action);
	}

	public void drawTooltip(int mouseX, int mouseY) {
		List<String> lines = getTooltip(mouseX, mouseY);
		dispatchEvent(new MouseEvent.TooltipEvent(mousedOverElement != null ? mousedOverElement : this, mouseX, mouseY, lines));
		if (!lines.isEmpty()) {
			GlStateManager.pushMatrix();
			ScaledResolution scaledresolution = new ScaledResolution(getMinecraft());
			GuiUtils.drawHoveringText(lines, mouseX - getX(), mouseY - getY(), scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), -1, getFontRenderer());
			GlStateManager.popMatrix();
		}
	}

	@Override
	public List<String> getTooltip(int mouseX, int mouseY) {
		List<String> tooltip = new ArrayList<>();
		Deque<IWidget> queue = this.calculateMousedOverElements();
		while (!queue.isEmpty()) {
			IWidget element = queue.removeFirst();
			if (element.isEnabled() && element.isVisible() && element.hasTooltip()) {
				tooltip.addAll(element.getTooltip(getRelativeMouseX(element), getRelativeMouseY(element)));
			}
		}
		return tooltip;
	}

	/* Mouse */
	public void setMousePosition(int mouseX, int mouseY) {
		float dx = (float) mouseX - (float) this.mouseX;
		float dy = (float) mouseY - (float) this.mouseY;
		if (dx != 0.0f || dy != 0.0f) {
			if (draggedElement != null) {
				dispatchEvent(new MouseEvent(draggedElement, MouseEvent.MOUSE_DRAG_MOVE, mouseX, mouseY, dx, dy));
			} else {
				dispatchEvent(new MouseEvent(this, MouseEvent.MOUSE_MOVE, mouseX, mouseY, dx, dy));
			}
		}
		if (mouseX != this.mouseX || mouseY != this.mouseY) {
			this.mouseX = mouseX;
			this.mouseY = mouseY;
			setMousedOverElement(calculateMousedOverElement());
		}
	}

	@Override
	public int getMouseX() {
		return mouseX;
	}

	@Override
	public int getMouseY() {
		return mouseY;
	}

	@Override
	public int getRelativeMouseX(@Nullable IWidget element) {
		if (element == null) {
			return mouseX;
		}
		return mouseX - element.getAbsoluteX();
	}

	@Override
	public int getRelativeMouseY(@Nullable IWidget element) {
		if (element == null) {
			return mouseY;
		}
		return mouseY - element.getAbsoluteY();
	}

	/* Gui Screen */
	@Override
	public int getScreenWidth() {
		return gui.width;
	}

	@Override
	public int getScreenHeight() {
		return gui.height;
	}

	@Override
	public int getGuiLeft() {
		return gui.getGuiLeft();
	}

	@Override
	public int getGuiTop() {
		return gui.getGuiTop();
	}

	@Override
	public G getGui() {
		return gui;
	}

	@Override
	public int getGuiHeight() {
		return gui.getSizeX();
	}

	@Override
	public int getGuiWidth() {
		return gui.getSizeY();
	}

	@Override
	public Optional<IWidget> getWidget(String name) {
		return Optional.ofNullable(widgets.get(name));
	}

	@Override
	public IWidget getWidgetOrEmpty(String name) {
		return getWidget(name).orElse(Widget.EMPTY);
	}

	public void addElement(IWidget widget) {
		String name = widget.getName();
		if (name != null && !widgets.containsKey(name)) {
			widgets.put(name, widget);
		}
		if (widget instanceof IWidgetGroup) {
			for (IWidget child : ((IWidgetGroup) widget).getElements()) {
				addElement(child);
			}
		}
	}

	protected Minecraft getMinecraft() {
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
		return mc;
	}

	@Override
	public TextureManager getTextureManager() {
		return getMinecraft().getTextureManager();
	}

	@Override
	public FontRenderer getFontRenderer() {
		return getMinecraft().fontRenderer;
	}

	@Override
	public void dispatchEvent(JEGEvent event, EventDestination destination) {
		try {
			destination.sendEvent(this, event);
			destination.sendEvent(event.getSource(), event);
		} catch (Exception e) {
			StringBuilder builder = new StringBuilder("/-----------------------------------------------------/").append('\n')
				.append("Failed to post event ('").append(event.getClass()).append("') to widget('").append(getName()).append("\')").append('\n');
			for (IWidget parent = this; parent != null; parent = parent.getParent()) {
				builder.append(parent.toString()).append('\n');
			}
			builder.append("/-----------------------------------------------------/").append('\n');
			System.out.print(builder.toString());
			e.printStackTrace();
		}
	}
}
