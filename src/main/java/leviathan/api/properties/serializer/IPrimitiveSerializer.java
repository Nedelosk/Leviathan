package leviathan.api.properties.serializer;

import javax.annotation.Nullable;

import leviathan.api.gui.IWindowWidget;

public interface IPrimitiveSerializer<V> extends IElementSerializer<V> {
	V parseValue(String s);

	default V parseValue(IWindowWidget window, String s) {
		return parseValue(s);
	}

	String toString(@Nullable V value);
}
