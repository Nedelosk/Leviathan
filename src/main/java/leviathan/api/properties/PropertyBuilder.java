package leviathan.api.properties;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import leviathan.api.properties.serializer.IElementSerializer;

public class PropertyBuilder<V, P> implements IPropertyBuilder<V, P> {
	private final PropertyType<V, P> type;
	private final String name;
	private final Function<P, V> valueProducer;
	private BiFunction<P, V, P> valueModifier;
	@Nullable
	private V defaultValue;
	private BiConsumer<IPropertyProvider<P>, P> valueUser;
	private Function<V, V> validator;
	@Nullable
	private IElementSerializer<V> serializer;

	public PropertyBuilder(PropertyType<V, P> type, String name, Function<P, V> valueProducer, BiFunction<P, V, P> valueModifier, @Nullable V defaultValue, BiConsumer<IPropertyProvider<P>, P> valueUser, Function<V, V> validator, @Nullable IElementSerializer<V> serializer) {
		this.type = type;
		this.name = name;
		this.valueProducer = valueProducer;
		this.valueModifier = valueModifier;
		this.defaultValue = defaultValue;
		this.valueUser = valueUser;
		this.validator = validator;
		this.serializer = serializer;
	}

	@Override
	public IPropertyBuilder<V, P> setDefaultValue(@Nullable V defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	@Override
	public IPropertyBuilder<V, P> setModifier(BiFunction<P, V, P> valueModifier) {
		this.valueModifier = valueModifier;
		return this;
	}

	@Override
	public IPropertyBuilder<V, P> setUser(BiConsumer<IPropertyProvider<P>, P> valueUser) {
		this.valueUser = valueUser;
		return this;
	}

	@Override
	public IPropertyBuilder<V, P> setValidator(Function<V, V> validator) {
		this.validator = validator;
		return this;
	}

	@Override
	public IPropertyBuilder<V, P> setSerializer(IElementSerializer<V> serializer) {
		this.serializer = serializer;
		return this;
	}

	@Override
	public IPropertyType<V, P> endProperty() {
		Preconditions.checkNotNull(defaultValue, "Failed to find default value for property ('" + name + "').");
		Preconditions.checkNotNull(serializer, "Failed to find serializer for property ('" + name + "').");
		type.collector.add(name, defaultValue, valueProducer, valueModifier, valueUser, validator, serializer);
		return type;
	}
}
