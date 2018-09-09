package leviathan.gui.properties;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;

import leviathan.api.properties.serializer.IRangeSerializer;

public class IntegerSerializer implements IRangeSerializer<Integer> {

	@Override
	public Integer parseValue(String s) {
		try {
			return Integer.valueOf(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public String toString(@Nullable Integer value) {
		return MoreObjects.firstNonNull(value, 0).toString();
	}

	@Override
	public Integer getNextValue(Integer value) {
		return value + 1;
	}

	@Override
	public Integer getPreviousValue(Integer value) {
		return value - 1;
	}

	/*@Override
	public Optional<Integer> readNBT(IWindowWidget window, NBTTagInt nbt) {
		return Optional.of(nbt.getInt());
	}

	@Override
	public NBTTagInt writeNBT(IWindowWidget window, Integer value) {
		return new NBTTagInt(value);
	}*/
}
