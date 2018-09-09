package leviathan.gui.properties;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;

import leviathan.api.properties.serializer.IPrimitiveSerializer;

public class StringSerializer implements IPrimitiveSerializer<String> {
	@Override
	public String parseValue(String s) {
		return s;
	}

	@Override
	public String toString(@Nullable String value) {
		return MoreObjects.firstNonNull(value, "");
	}

	/*@Override
	public Optional<String> readNBT(IWindowWidget window, NBTTagString nbt) {
		return Optional.of(nbt.getString());
	}

	@Override
	public NBTTagString writeNBT(IWindowWidget window, String value) {
		return new NBTTagString(value);
	}

	@Override
	public String getDefaultValue() {
		return "";
	}*/
}
