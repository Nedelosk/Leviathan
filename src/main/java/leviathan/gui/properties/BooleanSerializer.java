package leviathan.gui.properties;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;

import leviathan.api.properties.serializer.IRangeSerializer;

public class BooleanSerializer implements IRangeSerializer<Boolean> {

	@Override
	public Boolean parseValue(String value) {
		return Boolean.valueOf(value);
	}

	@Override
	public String toString(@Nullable Boolean value) {
		return MoreObjects.firstNonNull(value, false).toString();
	}

	@Override
	public Boolean getNextValue(Boolean value) {
		return !value;
	}

	@Override
	public Boolean getPreviousValue(Boolean value) {
		return !value;
	}

	/*@Override
	public Optional<Boolean> readNBT(IWindowWidget window, NBTTagByte nbt) {
		return Optional.of(nbt.getByte() == 1);
	}

	@Override
	public NBTTagByte writeNBT(IWindowWidget window, Boolean value) {
		return new NBTTagByte((byte)(value ? 1 : 0));
	}*/
}
