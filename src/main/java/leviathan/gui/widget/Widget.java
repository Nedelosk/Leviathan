package leviathan.gui.widget;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.Region;
import leviathan.api.gui.ICroppable;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.gui.events.ElementEvent;
import leviathan.api.gui.events.EventDestination;
import leviathan.api.gui.events.EventKey;
import leviathan.api.gui.events.IEventListener;
import leviathan.api.gui.events.IEventSystem;
import leviathan.api.gui.events.JEGEvent;
import leviathan.api.gui.tooltip.ITooltipSupplier;
import leviathan.api.properties.IProperty;
import leviathan.api.properties.IPropertyCollection;
import leviathan.api.properties.PropertyCollection;
import leviathan.gui.events.EventSystem;
import leviathan.gui.properties.JEGSerializers;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class Widget extends Gui implements IWidget {
	public static final IWidget EMPTY = EmptyWidget.INSTANCE;
	/* Attributes - Final */
	//Tooltip of the element
	private final List<ITooltipSupplier> tooltipSuppliers = new ArrayList<>();
	private final IEventSystem eventSystem;
	/* Attributes - State*/
	//Element Position
	protected Region region;
	private Region regionAbsolute = Region.EMPTY;

	//The start coordinates of the crop
	private Region croppedRegion = Region.EMPTY;
	//The element to that the crop coordinates are relative to.
	private IWidget cropElement = EMPTY;
	//Element Alignment relative to the parent
	private WidgetAlignment align = WidgetAlignment.TOP_LEFT;
	private boolean visible = true;
	private boolean enabled = true;
	private boolean valid;

	//The element container that contains this element
	@Nullable
	protected IWidget parent;
	protected String name;

	public Widget(int width, int height) {
		this(0, 0, width, height);
	}

	public Widget(int xPos, int yPos, int width, int height) {
		this.region = new Region(xPos, yPos, width, height);
		this.eventSystem = new EventSystem();
	}

	@Override
	public void addProperties(IPropertyCollection collection) {
		collection.addProperties(IWidget.class, creator ->
			creator.add("name", "Element", IWidget::getName, IWidget::setName, JEGSerializers.STRING)
				.add("region", Region.DEFAULT, IWidget::getRegion, IWidget::setRegion, JEGSerializers.REGION)
				.add("cropped_region", Region.EMPTY, ICroppable::getCroppedRegion, ICroppable::setCroppedRegion, JEGSerializers.REGION)
				.add("crop_element", EMPTY, IWidget::getCropElement, IWidget::setCropElement, JEGSerializers.WIDGET)
				.add("alignment", WidgetAlignment.TOP_LEFT, IWidget::getAlign, IWidget::setAlign, JEGSerializers.ALIGNMENT)
				.add("visible", true, IWidget::getVisible, IWidget::setVisible, JEGSerializers.BOOLEAN)
				.add("enabled", true, IWidget::getEnabled, IWidget::setEnabled, JEGSerializers.BOOLEAN));
	}

	@Override
	public IPropertyCollection getCollector() {
		PropertyCollection collection = new PropertyCollection();
		addProperties(collection);
		return collection;
	}

	@Override
	public IWidget getPropertyValue() {
		return this;
	}

	@Override
	public void setPropertyValue(IWidget value) {
		//
	}

	@Override
	public <V> void onValueChange(IProperty<V, IWidget> property, V oldValue, V value) {
		//Default-Implementation
	}

	@Override
	public void onCreation(IWindowWidget window) {
		//Default-Implementation
	}

	@Override
	public void onDeletion() {
		IWindowWidget window = getWindow();
		window.dispatchEvent(new ElementEvent(this, ElementEvent.DELETION));
	}

	@Override
	public Region getRegion() {
		return region;
	}

	@Override
	public IWidget setRegion(Region region) {
		Region oldRegion = this.region;
		this.region = region;
		this.regionAbsolute = Region.EMPTY;
		onRegionChange(oldRegion, region);
		if (hasWindow()) {
			getWindow().dispatchEvent(new ElementEvent.RegionChange(this, oldRegion, region));
		}
		return this;
	}

	protected void onRegionChange(Region oldRegion, Region newRegion) {
		//
	}

	@Override
	public void onParentRegionChange(Region oldRegion, Region newRegion) {
		regionAbsolute = Region.EMPTY;
		onRegionChange(region, region);
	}

	@Override
	public void validate() {
		if(!valid) {
			doValidate();
			valid = true;
		}
	}

	protected void doValidate(){
		//
	}

	@Override
	public void invalidate() {
		valid = false;
		if(parent != null){
			parent.invalidate();
		}
	}

	@Override
	public final int getX() {
		int x = 0;
		int parentWidth = parent != null ? parent.getWidth() : -1;
		int w = getWidth();
		if (parentWidth >= 0 && parentWidth > w) {
			x = (int) ((parentWidth - w) * align.getXOffset());
		}
		return region.getX() + x;
	}

	@Override
	public final int getY() {
		int y = 0;
		int parentHeight = parent != null ? parent.getHeight() : -1;
		int h = getHeight();
		if (parentHeight >= 0 && parentHeight > h) {
			y = (int) ((parentHeight - h) * align.getYOffset());
		}
		return region.getY() + y;
	}

	@Override
	public Region getAbsoluteRegion() {
		if (regionAbsolute.isEmpty()) {
			int x = getX();
			int y = getY();
			for (IWidget p = parent; p != null; p = p.getParent()) {
				x += p.getX();
				y += p.getY();
			}
			regionAbsolute = new Region(x, y, region.getWidth() + x, region.getHeight() + y);
		}
		return regionAbsolute;
	}

	public final int getAbsoluteX() {
		return getAbsoluteRegion().getX();
	}

	public final int getAbsoluteY() {
		return getAbsoluteRegion().getY();
	}

	@Override
	public final void draw(int mouseX, int mouseY) {
		if (!isVisible()) {
			return;
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(getX(), getY(), 0.0F);
		if (isCropped()) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution res = new ScaledResolution(mc);
			double scaleWidth = mc.displayWidth / res.getScaledWidth_double();
			double scaleHeight = mc.displayHeight / res.getScaledHeight_double();
			IWidget cropRelative = !cropElement.isEmpty() ? cropElement : this;
			int posX = cropRelative.getAbsoluteX();
			int posY = cropRelative.getAbsoluteY();
			GL11.glScissor((int) ((posX + croppedRegion.getX()) * scaleWidth), (int) (mc.displayHeight - ((posY + croppedRegion.getY() + croppedRegion.getHeight()) * scaleHeight)), (int) (croppedRegion.getWidth() * scaleWidth), (int) (croppedRegion.getHeight() * scaleHeight));
		}

		drawElement(mouseX, mouseY);

		if (isCropped()) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}

		GlStateManager.popMatrix();
	}

	public void drawElement(int mouseX, int mouseY) {
		//Default-Implementation
	}

	@Override
	public int getWidth() {
		return region.getWidth();
	}

	@Override
	public int getHeight() {
		return region.getHeight();
	}

	@Override
	public void setHeight(int height) {
		setSize(region.getWidth(), height);
	}

	@Override
	public void setWidth(int width) {
		setSize(width, region.getHeight());
	}

	@Override
	public IWidget setSize(int width, int height) {
		setRegion(region.withSize(width, height));
		return this;
	}

	@Override
	public void setName(String name) {
		if (hasWindow()) {
			ElementEvent.Rename event = new ElementEvent.Rename(this, this.name, name);
			getWindow().dispatchEvent(event);
			if (event.isCanceled()) {
				return;
			}
		}
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setXPosition(int xPos) {
		setLocation(xPos, region.getY());
	}

	@Override
	public void setYPosition(int yPos) {
		setLocation(region.getX(), yPos);
	}

	@Override
	public IWidget setLocation(int xPos, int yPos) {
		setRegion(region.withPosition(xPos, yPos));
		return this;
	}

	@Override
	public IWidget setBounds(int xPos, int yPos, int width, int height) {
		setRegion(new Region(xPos, yPos, width, height));
		return this;
	}

	@Override
	public IWidget setAlign(WidgetAlignment align) {
		this.align = align;
		this.regionAbsolute = Region.EMPTY;
		return this;
	}

	@Override
	public WidgetAlignment getAlign() {
		return align;
	}

	/* CROPPED */
	@Override
	public void setCroppedZone(@Nullable IWidget cropElement, Region coppedRegion) {
		this.cropElement = cropElement == null ? EMPTY : cropElement;
		this.croppedRegion = coppedRegion;
	}

	public void setCroppedRegion(Region coppedRegion) {
		this.croppedRegion = coppedRegion;
	}

	@Override
	public void setCropElement(@Nullable IWidget cropElement) {
		this.cropElement = cropElement == null ? EMPTY : cropElement;
	}

	@Override
	public IWidget getCropElement() {
		return cropElement;
	}

	public int getCropX() {
		return croppedRegion.getX();
	}

	public int getCropY() {
		return croppedRegion.getY();
	}

	public int getCropWidth() {
		return croppedRegion.getWidth();
	}

	public int getCropHeight() {
		return croppedRegion.getHeight();
	}

	@Override
	public Region getCroppedRegion() {
		return croppedRegion;
	}

	public boolean isCropped() {
		return !cropElement.isEmpty() && !croppedRegion.isEmpty();
	}

	@Override
	public IWindowWidget getWindow() {
		if (this.parent == null) {
			throw new IllegalStateException("Tried to access the window element of an element that doesn't had one.");
		} else {
			return this.parent.getWindow();
		}
	}

	public boolean hasWindow() {
		return parent != null && parent.hasWindow();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
		if (!isVisible()) {
			return false;
		}
		return mouseX >= 0 && mouseX < getWidth() && mouseY >= 0 && mouseY < getHeight();
	}

	@Override
	public final boolean isMouseOver() {
		IWindowWidget window = getWindow();
		int mouseX = window.getRelativeMouseX(this);
		int mouseY = window.getRelativeMouseY(this);
		if (!isCropped()) {
			return isMouseOver(mouseX, mouseY);
		}
		IWidget cropRelative = !cropElement.isEmpty() ? cropElement : this;
		int posX = cropRelative.getAbsoluteX() - this.getAbsoluteX();
		int posY = cropRelative.getAbsoluteY() - this.getAbsoluteY();
		boolean inCrop = mouseX >= posX && mouseY >= posY && mouseX <= posX + croppedRegion.getWidth() && mouseY <= posY + croppedRegion.getHeight();
		return inCrop && isMouseOver(mouseX, mouseY);
	}

	/**
	 * Called if this element get updated on the client side.
	 */
	@SideOnly(Side.CLIENT)
	protected void onUpdateClient() {
		//Default-Implementation
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateClient() {
		if (!this.isVisible()) {
			return;
		}
		this.onUpdateClient();
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean getVisible() {
		return visible;
	}

	@Override
	public boolean isVisible() {
		return visible && (parent == null || parent.isVisible());
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled && (parent == null || parent.isEnabled());
	}

	@Nullable
	@Override
	public IWidget getParent() {
		return parent;
	}

	@Override
	public IWidget setParent(@Nullable IWidget parent) {
		this.parent = parent;
		if (hasWindow()) {
			IWindowWidget window = getWindow();
			onCreation(window);
			window.dispatchEvent(new ElementEvent(this, ElementEvent.CREATION));
		}
		return this;
	}

	@Override
	public List<String> getTooltip(int mouseX, int mouseY) {
		List<String> lines = new ArrayList<>();
		tooltipSuppliers.stream().filter(ITooltipSupplier::hasTooltip).forEach(supplier -> supplier.addTooltip(lines, this, mouseX, mouseY));
		return lines;
	}

	@Override
	public IWidget addTooltip(String line) {
		addTooltip((tooltipLines, element, mouseX, mouseY) -> tooltipLines.add(line));
		return this;
	}

	@Override
	public IWidget addTooltip(Collection<String> lines) {
		addTooltip((tooltipLines, element, mouseX, mouseY) -> tooltipLines.addAll(lines));
		return this;
	}

	@Override
	public IWidget addTooltip(ITooltipSupplier supplier) {
		tooltipSuppliers.add(supplier);
		return this;
	}

	@Override
	public boolean hasTooltip() {
		return !tooltipSuppliers.isEmpty();
	}

	@Override
	public void clearTooltip() {
		tooltipSuppliers.clear();
	}

	@Override
	public List<String> getTooltip() {
		int mouseX = getWindow().getRelativeMouseX(this);
		int mouseY = getWindow().getRelativeMouseY(this);
		List<String> lines = new ArrayList<>();
		tooltipSuppliers.stream().filter(ITooltipSupplier::hasTooltip).forEach(supplier -> supplier.addTooltip(lines, this, mouseX, mouseY));
		return lines;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("name", name)
			.add("x", getX())
			.add("y", getY())
			.add("w", getWidth())
			.add("h", getHeight())
			.add("a", getAlign())
			.add("v", isVisible())
			.add("e", isEnabled())
			.toString();
	}

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
	public void dispatchEvent(JEGEvent event, EventDestination destination) {
		try {
			destination.sendEvent(this, event);
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

	@Override
	public void dispatchEvent(JEGEvent event, Collection<IWidget> widgets) {
		try {
			for (IWidget widget : widgets) {
				widget.receiveEvent(event);
			}
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
