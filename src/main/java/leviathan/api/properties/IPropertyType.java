package leviathan.api.properties;

import java.util.function.Function;

public interface IPropertyType<V, P> {
	IPropertyBuilder<V, P> startProp(String name, Function<P, V> valueProducer);

	IPropertyType<V, P> create(String name, Function<P, V> valueProducer);

	IPropertyCollector<P> endType();
}
