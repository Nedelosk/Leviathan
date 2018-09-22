/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package leviathan.api.gui;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.geometry.ITransformProvider;
import leviathan.api.geometry.Point;
import leviathan.api.geometry.Region;
import leviathan.api.geometry.RectTransform;
import leviathan.api.events.EventDestination;
import leviathan.api.events.IEventSystem;
import leviathan.api.events.JEGEvent;
import leviathan.api.tooltip.ITooltipSupplier;
import leviathan.api.properties.IProperty;
import leviathan.api.properties.IPropertyCollection;
import leviathan.api.properties.IPropertyProvider;

/**
 * A <em>gui element</em> is an object having a graphical representation that can be displayed on the screen and that
 * can interact with the user.
 * <p>
 * The interface the most gui elements of forestry implement. You must place the element a containment hierarchy whose
 * root is a top-level {@link IWindowWidget}.
 */
@SideOnly(Side.CLIENT)
public interface IWidget extends ICroppable, IPropertyProvider<IWidget>, IEventSystem, ITransformProvider {
	/* Position and Size*/

	/**
	 * @return the x position of this element relative to the position of its parent.
	 */
	int getX();

	/**
	 * @return the y position of this element relative to the position of its parent.
	 */
	int getY();

	String getName();

	void setName(String name);

	Region getRegion();

	IWidget setRegion(Region region);

	Region getAbsoluteRegion();

	default Point getPosition(){
		return getRegion().getPosition();
	}

	default Point getSize(){
		return getRegion().getSize();
	}

	default Point getAbsolutePosition(){
		return getAbsoluteRegion().getPosition();
	}

	void onParentRegionChange(Region oldRegion, Region newRegion);

	void validate();

	void invalidate();

	/**
	 * Sets the alignment of this element.
	 */
	IWidget setAlign(WidgetAlignment align);

	/**
	 * The alignment of the {@link IWidget} defines the position of the element relative to the position of its
	 * parent.
	 */
	WidgetAlignment getAlign();

	/**
	 * The root of the containment hierarchy that this element is part of.
	 */
	IWindowWidget getWindow();

	boolean hasWindow();

	boolean isEmpty();

	/**
	 * @return the x position of this element relative to the gui.
	 */
	int getAbsoluteX();

	/**
	 * @return the y position of this element relative to the gui.
	 */
	int getAbsoluteY();

	/**
	 * @return the size of the element on the x-axis.
	 */
	int getWidth();

	void setWidth(int width);

	/**
	 * @return the size of the element on the y-axis.
	 */
	int getHeight();

	void setHeight(int height);

	/**
	 * Sets the dimensions of this element.
	 *
	 * @param width  the size of the element on the x-axis.
	 * @param height the size of the element on the y-axis.
	 */
	IWidget setSize(int width, int height);

	default IWidget setSize(Point size){
		return setSize(size.getX(), size.getY());
	}

	/**
	 * Sets the position of this element.
	 *
	 * @param xPos the x position of this element relative to the position of its parent.
	 * @param yPos the y position of this element relative to the position of its parent.
	 */
	IWidget setLocation(int xPos, int yPos);

	/**
	 * Sets the dimensions and position of this element.
	 *
	 * @param xPos   the x position of this element relative to the position of its parent.
	 * @param yPos   the y position of this element relative to the position of its parent.
	 * @param width  the size of the element on the x-axis.
	 * @param height the size of the element on the y-axis.
	 */
	IWidget setBounds(int xPos, int yPos, int width, int height);

	/**
	 * Sets the x position of this element relative to the position of its parent.
	 */
	void setXPosition(int xPos);

	/**
	 * Sets the y position of this element relative to the position of its parent.
	 */
	void setYPosition(int yPos);

	/* Parent */

	/**
	 * The position of this element is relative to the position of its parent.
	 *
	 * @return the parent element of this element.
	 */
	@Nullable
	IWidgetContainer getParent();

	/**
	 * Sets the parent of this element.
	 */
	IWidget setParent(@Nullable IWidgetContainer parent);

	/* Creation & Deletion */

	/**
	 * Called at {@link IWidgetContainer#add(IWidget)} after the element was added to the group and
	 * {@link #setParent(IWidgetContainer)} was called at the element.
	 * <p>
	 * Can be used to add other element to the element if the element is an {@link IWidgetContainer}.
	 *
	 * @param window
	 */
	void onCreation(IWindowWidget window);

	/**
	 * Called at {@link IWidgetContainer#remove(IWidget...)} after the element was removed from the group.
	 */
	void onDeletion();

	/* Rendering */

	/**
	 * Draws the element and his children.
	 *
	 * @param mouseX The x position of the mouse relative to the parent of the element.
	 * @param mouseY The y position of the mouse relative to the parent of the element.
	 */
	void draw(int mouseX, int mouseY);

	/**
	 * Draws the element itself at the current position.
	 *
	 * @param mouseX The x position of the mouse relative to the parent of the element.
	 * @param mouseY The y position of the mouse relative to the parent of the element.
	 */
	void drawElement(int mouseX, int mouseY);

	/* Mouse Over */

	/**
	 * @param mouseX The x position of the mouse relative to the parent of the element.
	 * @param mouseY The y position of the mouse relative to the parent of the element.
	 * @return True if the mouse is currently over the element.
	 */
	boolean isMouseOver(int mouseX, int mouseY);

	boolean isMouseOver(Point mouse);

	/**
	 * @return True if the mouse is currently over the element.
	 */
	boolean isMouseOver();

	default boolean canMouseOver() {
		return true;
	}

	/* Updates */

	/**
	 * Updates the element. Called at {@link GuiScreen#updateScreen()}.
	 */
	@SideOnly(Side.CLIENT)
	void updateClient();

	/* State */

	/**
	 * @return True if this element can be focused and processes keys.
	 */
	default boolean canFocus() {
		return false;
	}

	default boolean isFocused() {
		if (getWindow() == null) {
			return false;
		}
		return getWindow().isFocused(this);
	}

	/**
	 * @return True of this element is currently visible.
	 */
	boolean isVisible();

	boolean getVisible();

	void setVisible(boolean visible);

	/**
	 * The most elements are enabled by default. Only a few elements are disabled at a certain time like buttons.
	 *
	 * @return True if this element is enabled.
	 */
	boolean isEnabled();

	boolean getEnabled();

	void setEnabled(boolean enabled);

	/* Tooltip */

	/**
	 * Adds an additional tooltip to the current tooltip of the element.
	 */
	IWidget addTooltip(String line);

	/**
	 * Adds an additional tooltip to the current tooltip of the element.
	 */
	IWidget addTooltip(Collection<String> lines);

	IWidget addTooltip(ITooltipSupplier supplier);

	/**
	 * @return True if this element currently has a tooltip.
	 */
	boolean hasTooltip();

	/**
	 * Clears the tooltips that were added with {@link #addTooltip(String)} and {@link #addTooltip(Collection)}.
	 * It does not remove default tooltips of an element like the fluid information of a tank or the item information
	 * of an slot element.
	 */
	void clearTooltip();

	/**
	 * Returns the tooltip that this element provides at the given mouse position.
	 *
	 * @param mouseX The x position of the mouse relative to the parent of the element.
	 * @param mouseY The y position of the mouse relative to the parent of the element.
	 */
	List<String> getTooltip(int mouseX, int mouseY);

	/**
	 * @return Returns the tooltips that were added with {@link #addTooltip(String)} and {@link #addTooltip(Collection)}.
	 */
	List<String> getTooltip();

	/**
	 * Receives an event and distributes them to the event handlers of this element.
	 */
	void receiveEvent(JEGEvent event);

	/* Properties */
	<V> void onValueChange(IProperty<V, IWidget> property, V oldValue, V value);


	void addProperties(IPropertyCollection properties);

	default void dispatchEvent(JEGEvent event) {
		dispatchEvent(event, EventDestination.ALL);
	}

	void dispatchEvent(JEGEvent event, EventDestination destination);

	void dispatchEvent(JEGEvent event, Collection<IWidget> widgets);

	default IWidget getCropRelative(){
		return getCroppedRegion().isEmpty() ? this : getCropElement();
	}

	RectTransform getTransform();
}
