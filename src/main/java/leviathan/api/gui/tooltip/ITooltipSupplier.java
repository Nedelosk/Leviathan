/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package leviathan.api.gui.tooltip;

import java.util.Collection;

import leviathan.api.gui.IWidget;

@FunctionalInterface
public interface ITooltipSupplier {

	default boolean hasTooltip() {
		return true;
	}

	void addTooltip(Collection<String> tooltip, IWidget element, int mouseX, int mouseY);
}
