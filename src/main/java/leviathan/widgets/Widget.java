package leviathan.widgets;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.client.renderer.GlStateManager;

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

	public Widget(String name, float x, float y, float width, float height) {
		this.name = name;
		this.transform = new RectTransform(this, x, y, width, height);
	}

	/* Rendering */
	@Override
	public final void draw() {
		if(!isVisible()){
			return;
		}
		Vector position = transform.getPosition();
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
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
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
		//Nothing
	}

	@Override
	public void onTransformParentChange() {
		//Nothing
	}

	@Override
	public Vector positionRelativeToWidget(Vector position) {
		return Vector.sub(position, positionOnScreen());
	}

	@Override
	public Vector positionOnScreen() {
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
	public List<String> getTooltip(Vector mousePosition) {
		Vector relativePosition = Vector.sub(mousePosition, positionOnScreen());
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
