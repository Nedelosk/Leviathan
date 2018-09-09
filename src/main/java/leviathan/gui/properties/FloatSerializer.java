package leviathan.gui.properties;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;

import leviathan.api.properties.serializer.IRangeSerializer;

public class FloatSerializer implements IRangeSerializer<Float> {
	@Override
	public Float parseValue(String value) {
		try {
			return Float.valueOf(value);
		} catch (NumberFormatException e) {
			return 0.0F;
		}
	}


	@Override
	public Float getNextValue(Float value) {
		return value + 0.1F;
	}

	@Override
	public Float getPreviousValue(Float value) {
		return value - 0.1F;
	}

	@Override
	public String toString(@Nullable Float value) {
		return MoreObjects.firstNonNull(value, 0.0F).toString();
	}

	/*@Override
	public Optional<Float> readNBT(IWindowWidget window, NBTTagFloat nbt) {
		return Optional.of(nbt.getFloat());
	}

	@Override
	public NBTTagFloat writeNBT(IWindowWidget window, Float value) {
		return new NBTTagFloat(value);
	}*/
}
