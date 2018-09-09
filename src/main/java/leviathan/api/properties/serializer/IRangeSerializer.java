package leviathan.api.properties.serializer;

public interface IRangeSerializer<V> extends IPrimitiveSerializer<V> {
	V getNextValue(V value);

	V getPreviousValue(V value);
}
