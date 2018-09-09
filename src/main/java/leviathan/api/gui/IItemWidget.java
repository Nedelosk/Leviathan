package leviathan.api.gui;

import net.minecraft.item.ItemStack;

/**
 * A element that contains and displays a {@link ItemStack}.
 */
public interface IItemWidget extends IWidget {
	/**
	 * @return The contained {@link ItemStack}.
	 */
	ItemStack getStack();

	IItemWidget setStack(ItemStack stack);
}
