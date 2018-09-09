package leviathan.api.properties;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import leviathan.api.properties.serializer.IElementSerializer;

public interface IPropertyCollector<P> {
	default <V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, BiFunction<P, V, P> valueModifier, BiConsumer<IPropertyProvider<P>, P> valueUser, IElementSerializer<V> serializer) {
		return add(name, defaultValue, valueProducer, valueModifier, valueUser, v -> v, serializer);
	}

	/*default <V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, BiConsumer<IPropertyProvider<P>, P> valueUser, IElementSerializer<V> serializer){
		return add(name, defaultValue, valueProducer, (p, v)->p, valueUser, serializer);
	}*/

	<V> IPropertyType<V, P> startType(@Nullable V defaultValue, @Nullable BiFunction<P, V, P> valueModifier, BiConsumer<IPropertyProvider<P>, P> defaultUser, Function<V, V> validator, @Nullable IElementSerializer<V> serializer);

	default <V> IPropertyType<V, P> startType(@Nullable V defaultValue, BiConsumer<IPropertyProvider<P>, P> defaultUser, Function<V, V> validator, @Nullable IElementSerializer<V> serializer) {
		return startType(defaultValue, null, defaultUser, validator, serializer);
	}

	default <V> IPropertyType<V, P> startType(@Nullable V defaultValue, BiConsumer<IPropertyProvider<P>, P> defaultUser, @Nullable IElementSerializer<V> serializer) {
		return startType(defaultValue, defaultUser, v -> v, serializer);
	}

	default <V> IPropertyType<V, P> startType(@Nullable V defaultValue, Function<V, V> validator, @Nullable IElementSerializer<V> serializer) {
		return startType(defaultValue, IPropertyProvider::setPropertyValue, validator, serializer);
	}

	default <V> IPropertyType<V, P> startType(@Nullable V defaultValue, @Nullable IElementSerializer<V> serializer) {
		return startType(defaultValue, v -> v, serializer);
	}

	default <V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, BiConsumer<P, V> valueConsumer, IElementSerializer<V> serializer) {
		return add(name, defaultValue, valueProducer, valueConsumer, v -> v, serializer);
	}

	<V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, IElementSerializer<V> serializer);

	<V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, BiConsumer<P, V> valueConsumer, Function<V, V> validator, IElementSerializer<V> serializer);

	<V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, BiFunction<P, V, P> valueModifier, BiConsumer<IPropertyProvider<P>, P> valueUser, Function<V, V> validator, IElementSerializer<V> serializer);
}
