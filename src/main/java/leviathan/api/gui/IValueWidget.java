package leviathan.api.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IValueWidget<V> extends IWidget {

	V getValue();

	void setValue(V value);
}
