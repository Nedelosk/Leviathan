package leviathan.gui.properties;

import javax.annotation.Nullable;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.properties.serializer.IPrimitiveSerializer;
import leviathan.gui.widget.Widget;

public class WidgetSerializer implements IPrimitiveSerializer<IWidget> {
	@Override
	public IWidget parseValue(String s) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IWidget parseValue(IWindowWidget window, String s) {
		if (s.isEmpty()) {
			return Widget.EMPTY;
		}
		return window.getWidgetOrEmpty(s);
	}

	@Override
	public String toString(@Nullable IWidget value) {
		if (value == null || value.isEmpty()) {
			return "";
		}
		return value.getName();
	}

	/*@Override
	public Optional<IWidget> readNBT(IWindowWidget window, NBTTagString nbt) {
		return parseValue(nbt.getString());
	}

	@Override
	public NBTTagString writeNBT(IWindowWidget window, IWidget value) {
		return new NBTTagString(value.getName());
	}*/
}
