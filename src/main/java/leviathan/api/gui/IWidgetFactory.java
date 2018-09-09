/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package leviathan.api.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.style.ITextStyle;

/**
 * A helper interface to create gui elements.
 */
@SideOnly(Side.CLIENT)
public interface IWidgetFactory {

	ITextStyle getGuiStyle();

	/* LAYOUTS */
	IWidgetLayout createHorizontal(int xPos, int yPos, int height);

	IWidgetLayout createVertical(int xPos, int yPos, int width);

	IWidgetGroup createPane(int xPos, int yPos, int width, int height);
}
