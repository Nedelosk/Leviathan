package leviathan.api.properties;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import leviathan.api.properties.serializer.IElementSerializer;

public interface IPropertyBuilder<V, P> {

	IPropertyBuilder<V, P> setDefaultValue(V defaultValue);

	IPropertyBuilder<V, P> setModifier(BiFunction<P, V, P> valueModifier);

	IPropertyBuilder<V, P> setUser(BiConsumer<IPropertyProvider<P>, P> valueUser);

	IPropertyBuilder<V, P> setValidator(Function<V, V> validator);

	IPropertyBuilder<V, P> setSerializer(IElementSerializer<V> serializer);

	IPropertyType<V, P> endProperty();

}
