package leviathan.widgets;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.client.renderer.GlStateManager;

import leviathan.api.events.ElementEvent;
import leviathan.api.geometry.Point;
import leviathan.api.geometry.RectTransform;
import leviathan.api.geometry.Vector;
import leviathan.api.events.EventKey;
import leviathan.api.events.IEventListener;
import leviathan.api.events.IEventSystem;
import leviathan.api.events.JEGEvent;
import leviathan.api.tooltip.ITooltipSupplier;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.ILayout;
import leviathan.api.widgets.IWidget;
import leviathan.api.widgets.IWindow;
import leviathan.gui.events.EventSystem;

public class Widget implements IWidget {
	private final RectTransform transform;
	private final List<ITooltipSupplier> tooltipSuppliers = new ArrayList<>();
	private final IEventSystem eventSystem = new EventSystem();
	@Nullable
	private IContainer container;
	private boolean visible = true;
	private boolean enabled = true;
	private boolean valid = false;
	private String name;

	public Widget(String name, int x, int y, int width, int height) {
		this.name = name;
		this.transform = new RectTransform(this, x, y, width, height);
	}

	/* Rendering */
	@Override
	public final void draw() {
		if(!isVisible()){
			return;
		}
		Point position = transform.getPosition();
		Vector scale = transform.getScale();
		GlStateManager.pushMatrix();
		GlStateManager.translate(position.x, position.y, 1.0F);
		GlStateManager.scale(scale.x, scale.y, 1.0F);
		drawWidget();
		GlStateManager.popMatrix();
	}

	protected void drawWidget(){
	}

	@Override
	public void updateClient() {
		if(!isVisible()){
			return;
		}
		onClientUpdate();
	}

	protected void onClientUpdate(){
		//Nothing
	}

	/* State */

	@Override
	public void onCreation() {
		//TODO Find a method so this event gets to the window even if the parent gets later added to the window than this widget to the parent
		actOnWindow(window -> window.dispatchEvent(new ElementEvent(this, ElementEvent.CREATION)));
	}

	@Override
	public void onDeletion() {
		actOnWindow(window -> window.dispatchEvent(new ElementEvent(this, ElementEvent.DELETION)));
	}

	@Override
	public boolean isRecursivelyVisible() {
		return visible && getContainer().map(IWidget::isRecursivelyVisible).orElse(true);
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		onNameChange(oldName, name);
	}

	@Override
	public String getName() {
		return name;
	}

	protected void onNameChange(String oldName, String newName) {
		actOnWindow(window -> window.dispatchEvent(new ElementEvent.Rename(this, oldName, name)));
	}

	@Override
	public void onParentNameChange(String oldName, String newName) {
		//Nothing
	}

	@Override
	public boolean isRecursivelyEnabled() {
		return enabled && getContainer().map(IWidget::isRecursivelyEnabled).orElse(true);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/* Container */
	@Override
	public Optional<IContainer> getContainer() {
		return Optional.ofNullable(container);
	}

	@Override
	public void setContainer(@Nullable IContainer container) {
		this.container = container;
		if(container != null) {
			this.transform.setParent(container.getTransform());
		}else{
			this.transform.setParent(null);
		}
	}

	@Override
	public Optional<IWindow> getContainingWindow() {
		return getContainer().flatMap(IWidget::getContainingWindow);
	}

	@Override
	public void actOnWindow(Consumer<IWindow> windowConsumer) {
		getContainingWindow().ifPresent(windowConsumer);
	}

	/* Transform */
	@Override
	public RectTransform getTransform() {
		return transform;
	}

	@Override
	public void onTransformChange() {
		actOnWindow(window -> window.dispatchEvent(new ElementEvent.RegionChange(this)));
	}

	@Override
	public void onTransformParentChange() {
		//Nothing
	}

	@Override
	public Point positionRelativeToWidget(Point position) {
		return Point.sub(position, positionOnScreen());
	}

	@Override
	public Point positionOnScreen() {
		return transform.getScreenPosition();
	}

	/* Layout */
	@Override
	public void validate() {
		if(!valid){
			doLayout();
			valid = true;
		}
	}

	protected void doLayout(){
		//Nothing
	}

	@Override
	public void invalidate() {
		valid = false;
		getContainer().ifPresent(ILayout::invalidate);
	}

	/* Tooltip */
	@Override
	public boolean hasTooltip() {
		return tooltipSuppliers.isEmpty();
	}

	@Override
	public void clearTooltip() {
		tooltipSuppliers.clear();
	}

	@Override
	public void addTooltip(ITooltipSupplier supplier) {
		tooltipSuppliers.add(supplier);
	}

	@Override
	public List<String> getTooltip(Point mousePosition) {
		Point relativePosition = Point.sub(mousePosition, positionOnScreen());
		List<String> lines = new ArrayList<>();
		tooltipSuppliers.stream().filter(ITooltipSupplier::hasTooltip).forEach(supplier -> supplier.addTooltip(lines, this, relativePosition));
		return lines;
	}

	/* Events */
	@Override
	public <E extends JEGEvent> void addListener(EventKey<E> key, IEventListener<E> listener) {
		eventSystem.addListener(key, listener);
	}

	@Override
	public boolean hasListener(EventKey key) {
		return eventSystem.hasListener(key);
	}

	@Override
	public <E extends JEGEvent> void removeListener(EventKey<E> key, IEventListener<E> listener) {
		eventSystem.removeListener(key, listener);
	}

	@Override
	public void receiveEvent(JEGEvent event) {
		eventSystem.receiveEvent(event);
	}

	@Override
	public IEventSystem getEventSystem() {
		return eventSystem;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("name", name)
			.add("v", isVisible())
			.add("e", isEnabled())
			.add("t", transform)
			.toString();
	}
}
