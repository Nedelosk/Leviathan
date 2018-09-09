package leviathan.gui.widget;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import leviathan.api.Region;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.gui.events.EventDestination;
import leviathan.api.gui.events.EventKey;
import leviathan.api.gui.events.IEventListener;
import leviathan.api.gui.events.JEGEvent;
import leviathan.api.gui.tooltip.ITooltipSupplier;
import leviathan.api.properties.IProperty;
import leviathan.api.properties.IPropertyCollection;
import leviathan.api.properties.PropertyCollection;

public class EmptyWidget implements IWidget {
	public static final String NAME = "empty";
	public static final EmptyWidget INSTANCE = new EmptyWidget();

	private EmptyWidget() {
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public Region getRegion() {
		return Region.EMPTY;
	}

	@Override
	public IWidget setRegion(Region region) {
		return this;
	}

	@Override
	public Region getAbsoluteRegion() {
		return Region.EMPTY;
	}

	@Override
	public void onParentRegionChange(Region oldRegion, Region newRegion) {
	}

	@Override
	public IWidget setAlign(WidgetAlignment align) {
		return this;
	}

	@Override
	public WidgetAlignment getAlign() {
		return WidgetAlignment.TOP_LEFT;
	}

	@Override
	public IWindowWidget getWindow() {
		throw new IllegalArgumentException("getWindow() was called at an empty widget. Please call isEmpty() before you call getWindow().");
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean hasWindow() {
		return false;
	}

	@Override
	public int getAbsoluteX() {
		return 0;
	}

	@Override
	public int getAbsoluteY() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public void setWidth(int width) {
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void setHeight(int height) {

	}

	@Override
	public IWidget setSize(int width, int height) {
		return this;
	}

	@Override
	public IWidget setLocation(int xPos, int yPos) {
		return this;
	}

	@Override
	public IWidget setOffset(int xOffset, int yOffset) {
		return this;
	}

	@Override
	public IWidget setBounds(int xPos, int yPos, int width, int height) {
		return this;
	}

	@Override
	public void setXPosition(int xPos) {

	}

	@Override
	public void setYPosition(int yPos) {

	}

	@Nullable
	@Override
	public IWidget getParent() {
		return null;
	}

	@Override
	public IWidget setParent(@Nullable IWidget parent) {
		return this;
	}

	@Override
	public void onCreation(IWindowWidget window) {

	}

	@Override
	public void onDeletion() {

	}

	@Override
	public void draw(int mouseX, int mouseY) {

	}

	@Override
	public void drawElement(int mouseX, int mouseY) {

	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
		return false;
	}

	@Override
	public boolean isMouseOver() {
		return false;
	}

	@Override
	public void updateClient() {

	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public boolean getVisible() {
		return false;
	}

	@Override
	public void setVisible(boolean visible) {

	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public boolean getEnabled() {
		return false;
	}

	@Override
	public void setEnabled(boolean enabled) {

	}

	@Override
	public IWidget addTooltip(String line) {
		return this;
	}

	@Override
	public IWidget addTooltip(Collection<String> lines) {
		return this;
	}

	@Override
	public IWidget addTooltip(ITooltipSupplier supplier) {
		return this;
	}

	@Override
	public boolean hasTooltip() {
		return false;
	}

	@Override
	public void clearTooltip() {

	}

	@Override
	public List<String> getTooltip(int mouseX, int mouseY) {
		return Collections.emptyList();
	}

	@Override
	public List<String> getTooltip() {
		return Collections.emptyList();
	}

	@Override
	public <E extends JEGEvent> void addListener(EventKey<E> key, IEventListener<E> listener) {

	}

	@Override
	public boolean hasListener(EventKey key) {
		return false;
	}

	@Override
	public <E extends JEGEvent> void removeListener(EventKey<E> key, IEventListener<E> listener) {

	}

	@Override
	public void receiveEvent(JEGEvent event) {

	}

	@Override
	public void dispatchEvent(JEGEvent event, EventDestination destination) {

	}

	@Override
	public void dispatchEvent(JEGEvent event, Collection<IWidget> widgets) {

	}

	@Override
	public <V> void onValueChange(IProperty<V, IWidget> property, V oldValue, V value) {

	}

	@Override
	public void addProperties(IPropertyCollection properties) {

	}

	@Override
	public void setCroppedZone(@Nullable IWidget cropElement, Region coppedRegion) {

	}

	@Override
	public void setCropElement(@Nullable IWidget cropElement) {

	}

	@Override
	public void setCroppedRegion(Region coppedRegion) {

	}

	@Override
	public IWidget getCropElement() {
		return Widget.EMPTY;
	}

	@Override
	public int getCropX() {
		return 0;
	}

	@Override
	public int getCropY() {
		return 0;
	}

	@Override
	public int getCropWidth() {
		return 0;
	}

	@Override
	public int getCropHeight() {
		return 0;
	}

	@Override
	public Region getCroppedRegion() {
		return Region.EMPTY;
	}

	@Override
	public boolean isCropped() {
		return false;
	}

	@Override
	public IPropertyCollection getCollector() {
		return new PropertyCollection();
	}

	@Override
	public IWidget getPropertyValue() {
		return this;
	}

	@Override
	public void setPropertyValue(IWidget value) {

	}
}
