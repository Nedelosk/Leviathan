package leviathan.api.events;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.IWidget;


@SideOnly(Side.CLIENT)
public class ValueChangedEvent<V> extends JEGEvent {
	private final V newValue;
	private final V oldValue;

	public ValueChangedEvent(IWidget origin, EventKey key, V newValue, V oldValue) {
		super(origin, key);
		this.newValue = newValue;
		this.oldValue = oldValue;
	}

	public V getNewValue() {
		return newValue;
	}

	public V getOldValue() {
		return oldValue;
	}
}
