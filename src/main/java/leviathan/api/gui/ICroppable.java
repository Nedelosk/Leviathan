/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package leviathan.api.gui;

import javax.annotation.Nullable;

import leviathan.api.Region;

public interface ICroppable {
	/**
	 * Sets the cropped zone of this element and the element the zone is relative to.
	 *
	 * @param cropElement The element the zone is relative to.
	 * @param cropX       The x start coordinate of the zone.
	 * @param cropY       The y start coordinate of the zone.
	 * @param cropWidth   The width of the zone.
	 * @param cropHeight  The height of the zone.
	 */
	void setCroppedZone(@Nullable IWidget cropElement, Region coppedRegion);

	void setCropElement(@Nullable IWidget cropElement);

	void setCroppedRegion(Region coppedRegion);

	/**
	 * @return The element the cropped zone is relative to.
	 */
	IWidget getCropElement();

	/**
	 * @return The x start coordinate of the cropped zone.
	 */
	int getCropX();

	/**
	 * @return The y start coordinate of the cropped zone.
	 */
	int getCropY();

	/**
	 * @return The width of the cropped zone.
	 */
	int getCropWidth();

	/**
	 * @return The height of the cropped zone.
	 */
	int getCropHeight();

	Region getCroppedRegion();

	/**
	 * @return True if {@link #setCroppedZone(IWidget, Region)} was ever called on this element.
	 */
	boolean isCropped();
}
