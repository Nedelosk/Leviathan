package leviathan.widgets;

import javax.annotation.Nullable;
import java.awt.Rectangle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.GuiScreen;

import leviathan.api.events.FocusEvent;
import leviathan.api.events.MouseEvent;
import leviathan.api.geometry.RectTransform;
import leviathan.api.geometry.Vector;
import leviathan.api.events.JEGEvent;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IScreen;
import leviathan.api.widgets.IScreenLayout;
import leviathan.api.widgets.ITooltipProvider;
import leviathan.api.widgets.IWidget;
import leviathan.api.widgets.IWindow;

public class Screen implements IScreen {
	private final Map<String, IWindow> windows = new LinkedHashMap<>();
	private final GuiScreen gui;
	private final IScreenLayout layout;
	/* Size */
	private int width;
	private int height;
	/* Mouse */
	private Vector mousePosition = new Vector(-1, -1);
	private Deque<IWidget> hoveredWidgets = new ArrayDeque<>();
	@Nullable
	private IWidget focusedWidget;
	@Nullable
	private IWidget draggedWidget;

	public Screen(GuiScreen gui, IScreenLayout layout) {
		this.gui = gui;
		this.layout = layout;
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		layout.layoutWindows(this, windows.values());
	}

	@Override
	public void updateScreen(int mouseX, int mouseY) {
		updateMousePosition(new Vector(mouseX, mouseY));
	}

	@Override
	public void drawWindows() {
		windows.values().forEach(IWidget::draw);
	}

	protected void updateMousePosition(Vector mousePosition){
		hoveredWidgets.clear();
		Optional<IWindow> windowOptional = getWindowUnderMouse();
		windowOptional.ifPresent(window->{
			Collection<IWidget> collectedWidgets = collectHoverWidgets(window);
			collectedWidgets.stream().filter(this::isMouseOver).forEach(widget-> hoveredWidgets.addLast(widget));
		});
		Vector delta = Vector.sub(mousePosition, this.mousePosition);
		if(!delta.equals(Vector.ORIGIN)){
			getWindowUnderMouse().ifPresent(window->{
				window.dispatchEvent(new MouseEvent(window, MouseEvent.MOUSE_MOVE, mousePosition, delta));
				if(draggedWidget != null){
					draggedWidget.dispatchEvent(new MouseEvent(draggedWidget, MouseEvent.MOUSE_DRAG_MOVE, mousePosition, delta));
				}
			});
		}
		this.mousePosition = mousePosition;
	}

	/*public void setMousePosition(int mouseX, int mouseY) {
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
	}*/

	protected Collection<IWidget> collectHoverWidgets(IWidget widget){
		if(!widget.isVisible()|| !widget.isEnabled()){
			return Collections.emptyList();
		}
		List<IWidget> widgets = new ArrayList<>();
		if(widget instanceof IContainer){
			for(IWidget child : ((IContainer) widget).getChildren()){
				widgets.addAll(collectHoverWidgets(child));
			}
		}
		widgets.add(widget);
		return widgets;
	}

	public Collection<IWidget> getHoveredWidgets() {
		return hoveredWidgets;
	}

	@Override
	public Vector getSize() {
		return new Vector(width, height);
	}

	@Override
	public void addWindow(String name, IWindow windowWidget) {
		windows.put(name, windowWidget);
	}

	@Override
	public IWindow createWindow(String name, int width, int height) {
		IWindow window = new Window(this, name, width, height);
		addWindow(name, window);
		return window;
	}

	@Override
	public Optional<IWindow> getWindow(String name) {
		return Optional.empty();
	}

	@Override
	public Optional<IWidget> getWidget(String windowName, String widgetName) {
		return Optional.empty();
	}

	@Override
	public Collection<IWindow> getWindows() {
		return windows.values();
	}

	public boolean isMouseOverImpl(IWidget widget){
		RectTransform widgetTransform = widget.getTransform();
		Vector relativePosition = Vector.sub(mousePosition, widgetTransform.getScreenPosition());
		return widgetTransform.contains(relativePosition);
	}

	@Override
	public boolean isMouseOver(IWidget widget) {
		return hoveredWidgets.contains(widget);
	}

	@Override
	public boolean isFocused(IWidget widget) {
		return widget == focusedWidget;
	}

	@Override
	public boolean isDragged(IWidget widget) {
		return widget == draggedWidget;
	}

	@Nullable
	public IWidget getDraggedWidget() {
		return this.draggedWidget;
	}

	public void setDraggedWidget(@Nullable IWidget widget) {
		this.setDraggedWidget(widget, -1);
	}

	public void setDraggedWidget(@Nullable IWidget widget, int button) {
		if (this.draggedWidget == widget) {
			return;
		}
		IWidget oldDragged = draggedWidget;
		this.draggedWidget = widget;
		if (oldDragged != null) {
			oldDragged.actOnWindow(window-> window.dispatchEvent(new MouseEvent(oldDragged, MouseEvent.MOUSE_DRAG_END, mousePosition, button)));
		}
		if (this.draggedWidget != null) {
			draggedWidget.actOnWindow(window-> window.dispatchEvent(new MouseEvent(this.draggedWidget, MouseEvent.MOUSE_DRAG_START, mousePosition, button)));
		}
	}

	@Nullable
	public IWidget getFocusedWidget() {
		return this.focusedWidget;
	}

	public void setFocusedWidget(@Nullable IWidget widget) {
		IWidget newElement = widget;
		if (this.focusedWidget == newElement) {
			return;
		}
		if (newElement != null && !newElement.canFocus()) {
			newElement = null;
		}
		if (this.focusedWidget != null) {
			focusedWidget.actOnWindow(window-> window.dispatchEvent(new FocusEvent(this.focusedWidget, FocusEvent.LOSE)));
		}
		this.focusedWidget = newElement;
		if (this.focusedWidget != null) {
			focusedWidget.actOnWindow(window-> window.dispatchEvent(new FocusEvent(this.focusedWidget, FocusEvent.GAIN)));
		}
	}

	@Override
	public boolean isOverWindowArea(Vector position) {
		return getWindowAreas().stream().anyMatch(windowPair -> windowPair.getValue().contains(Math.round(position.x), Math.round(position.y)));
	}

	@Override
	public Collection<Pair<IWindow, Rectangle>> getWindowAreas() {
		return windows.values().stream().map(window->Pair.of(window, window.getTransform().toRectangle())).collect(Collectors.toList());
	}

	@Override
	public Optional<IWindow> getWindowUnderMouse() {
		return getWindowUnderPosition(mousePosition);
	}

	@Override
	public Optional<IWindow> getWindowUnderPosition(Vector position) {
		return getWindowAreas().stream().filter(windowPair->windowPair.getValue().contains(Math.round(position.x), Math.round(position.y))).map(Pair::getKey).findAny();
	}

	@Override
	public <E extends JEGEvent> void sendEventToHover(Predicate<IWidget> widgetPredicate, Function<IWidget, E> eventFactory, boolean single) {
		hoveredWidgets.stream().filter(widgetPredicate).limit(single ? 1 : hoveredWidgets.size()).forEach(widget -> widget.dispatchEvent(eventFactory.apply(widget)));
	}

	@Override
	public List<String> getTooltip() {
		/*List<String> tooltip = new ArrayList<>();
		Deque<leviathan.api.gui.IWidget> queue = this.calculateMousedOverElements();
		while (!queue.isEmpty()) {
			leviathan.api.gui.IWidget element = queue.removeFirst();
			if (element.isEnabled() && element.isVisible() && element.hasTooltip()) {
				tooltip.addAll(element.getTooltip(getRelativeMouseX(element), getRelativeMouseY(element)));
			}
		}
		return tooltip;*/
		return hoveredWidgets.stream().filter(ITooltipProvider::hasTooltip).flatMap(widget -> widget.getTooltip(mousePosition).stream()).collect(Collectors.toList());
	}

	@Override
	public Vector getMousePosition() {
		return mousePosition;
	}
}
