/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package leviathan.gui.widget;

import net.minecraft.item.ItemStack;

import leviathan.api.gui.IItemWidget;
import leviathan.api.properties.IPropertyCollection;
import leviathan.gui.properties.JEGSerializers;

public class ItemWidget extends AbstractItemWidget implements IItemWidget {
	/* Attributes */
	private ItemStack stack;

	public ItemWidget(int xPos, int yPos, ItemStack stack) {
		super(xPos, yPos);
		this.stack = stack;
	}

	@Override
	public void addProperties(IPropertyCollection collection) {
		super.addProperties(collection);
		collection.addProperties(ItemWidget.class, creator -> creator.add("item", ItemStack.EMPTY, ItemWidget::getStack, ItemWidget::setStack, JEGSerializers.ITEM_STACK));
	}

	@Override
	public ItemStack getStack() {
		return stack;
	}

	@Override
	public IItemWidget setStack(ItemStack stack) {
		this.stack = stack;
		return this;
	}
}
