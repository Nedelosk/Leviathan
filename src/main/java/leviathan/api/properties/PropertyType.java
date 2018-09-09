package leviathan.api.properties;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import leviathan.api.properties.serializer.IElementSerializer;

public class PropertyType<V, P> implements IPropertyType<V, P> {
	final IPropertyCollector<P> collector;
	@Nullable
	private final V defaultValue;
	private final BiFunction<P, V, P> valueModifier;
	private final BiConsumer<IPropertyProvider<P>, P> defaultUser;
	private final Function<V, V> validator;
	@Nullable
	private final IElementSerializer<V> serializer;

	public PropertyType(IPropertyCollector<P> collector, @Nullable V defaultValue, BiFunction<P, V, P> valueModifier, BiConsumer<IPropertyProvider<P>, P> defaultUser, Function<V, V> validator, @Nullable IElementSerializer<V> serializer) {
		this.collector = collector;
		this.valueModifier = valueModifier;
		this.defaultValue = defaultValue;
		this.defaultUser = defaultUser;
		this.validator = validator;
		this.serializer = serializer;
	}

	@Override
	public IPropertyBuilder<V, P> startProp(String name, Function<P, V> valueProducer) {
		return new PropertyBuilder<>(this, name, valueProducer, valueModifier, defaultValue, defaultUser, validator, serializer);
	}

	@Override
	public IPropertyType<V, P> create(String name, Function<P, V> valueProducer) {
		return startProp(name, valueProducer).endProperty();
	}

	@Override
	public IPropertyCollector<P> endType() {
		return collector;
	}
}
