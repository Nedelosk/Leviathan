/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package leviathan.api.gui;

import javax.annotation.Nullable;

import leviathan.api.geometry.Region;

public interface ICroppable {
	/**
	 * Sets the cropped zone of this element and the element the zone is relative to.
	 *
	 * @param cropElement The element the zone is relative to.
	 */
	void setCroppedZone(@Nullable IWidget cropElement, Region coppedRegion);

	void setCropElement(@Nullable IWidget cropElement);

	void setCroppedRegion(Region coppedRegion);

	/**
	 * @return The element the cropped zone is relative to.
	 */
	IWidget getCropElement();

	Region getCroppedRegion();

	/**
	 * @return True if {@link #setCroppedZone(IWidget, Region)} was ever called on this element.
	 */
	boolean isCropped();
}
