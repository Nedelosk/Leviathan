package leviathan.gui.properties;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;

import leviathan.api.properties.serializer.IRangeSerializer;

public class EnumSerializer<V extends Enum<V>> implements IRangeSerializer<V> {
	private final Class<V> enumClass;
	private final V[] enumConstants;

	public EnumSerializer(Class<V> enumClass) {
		this.enumClass = enumClass;
		this.enumConstants = enumClass.getEnumConstants();
	}

	@Override
	public V getNextValue(Enum value) {
		int index = value.ordinal() + 1;
		if (index >= enumConstants.length) {
			index = 0;
		}
		return enumConstants[index];
	}

	@Override
	public V getPreviousValue(Enum value) {
		int index = value.ordinal() - 1;
		if (index < 0) {
			index = enumConstants.length - 1;
		}
		return enumConstants[index];
	}

	@Override
	public V parseValue(String value) {
		try {
			return Enum.valueOf(enumClass, value);
		} catch (NullPointerException e) {
			return enumConstants[0];
		}
	}

	@Override
	public String toString(@Nullable V value) {
		return MoreObjects.firstNonNull(value, enumConstants[0]).name();
	}

	/*@Override
	public Optional<V> readNBT(IWindowWidget window, NBTTagString nbt) {
		return parseValue(nbt.getString());
	}

	@Override
	public NBTTagString writeNBT(IWindowWidget window, V value) {
		return new NBTTagString(toString(value));
	}*/
}
