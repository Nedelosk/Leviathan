package leviathan.api.properties;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import leviathan.api.properties.serializer.IElementSerializer;
import leviathan.api.properties.serializer.IPropertySerializer;

public class PropertyCollector<P> implements IPropertyCollector<P> {
	public final List<IProperty<?, P>> properties = new ArrayList<>();

	private final IPropertyProvider<P> provider;

	public PropertyCollector(IPropertyProvider<P> provider) {
		this.provider = provider;
	}

	@Override
	public <V> IPropertyType<V, P> startType(@Nullable V defaultValue, @Nullable BiFunction<P, V, P> valueModifier, BiConsumer<IPropertyProvider<P>, P> defaultUser, Function<V, V> validator, @Nullable IElementSerializer<V> serializer) {
		BiFunction<P, V, P> modifier = valueModifier;
		if (modifier == null) {
			Preconditions.checkArgument(provider instanceof IProperty);
			IProperty<P, ?> property = (IProperty<P, ?>) provider;
			IElementSerializer<P> serial = property.getSerializer();
			Preconditions.checkArgument(serial instanceof IPropertySerializer);
			IPropertySerializer<P> propertySerializer = (IPropertySerializer<P>) serial;
			modifier = (p, v) -> propertySerializer.createValue(property);
		}
		return new PropertyType<>(this, defaultValue, modifier, defaultUser, validator, serializer);
	}

	@Override
	public <V> IPropertyType<V, P> startType(@Nullable V defaultValue, BiConsumer<IPropertyProvider<P>, P> defaultUser, Function<V, V> validator, @Nullable IElementSerializer<V> serializer) {
		return startType(defaultValue, null, defaultUser, validator, serializer);
	}

	@Override
	public <V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, IElementSerializer<V> serializer) {
		return add(name, defaultValue, valueProducer, (p, v) -> provider.setPropertyValue(p), serializer);
	}

	@Override
	public <V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, BiConsumer<P, V> valueConsumer, Function<V, V> validator, IElementSerializer<V> serializer) {
		Property<V, P> property = new Property<>(provider, name, defaultValue, valueProducer, valueConsumer, validator, serializer);
		properties.add(property);
		return this;
	}

	@Override
	public <V> IPropertyCollector<P> add(String name, V defaultValue, Function<P, V> valueProducer, BiFunction<P, V, P> valueModifier, BiConsumer<IPropertyProvider<P>, P> valueUser, Function<V, V> validator, IElementSerializer<V> serializer) {
		Property<V, P> property = new Property<>(provider, name, defaultValue, valueProducer, valueModifier, valueUser, validator, serializer);
		properties.add(property);
		return this;
	}
}
