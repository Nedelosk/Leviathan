package leviathan.api.properties;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import leviathan.api.properties.serializer.IElementSerializer;
import leviathan.api.properties.serializer.IPrimitiveSerializer;
import leviathan.api.properties.serializer.IPropertySerializer;
import leviathan.api.properties.serializer.IRangeSerializer;

public class Property<V, P> implements IProperty<V, P> {
	private final IPropertyProvider<P> parent;
	private final String name;
	private final V defaultValue;
	private final Function<P, V> valueProducer;
	private final BiConsumer<P, V> valueConsumer;
	private final Function<V, V> validator;
	private final IElementSerializer<V> serializer;
	private final Map<String, IProperty> children = new HashMap<>();
	private final IPropertyCollection collection;

	private V cachedValue;
	private String text;

	public Property(IPropertyProvider<P> parent, String name, V defaultValue, Function<P, V> valueProducer, BiFunction<P, V, P> valueModifier, BiConsumer<IPropertyProvider<P>, P> valueUser, IElementSerializer<V> serializer) {
		this(parent, name, defaultValue, valueProducer, valueModifier, valueUser, v -> v, serializer);
	}

	public Property(IPropertyProvider<P> parent, String name, V defaultValue, Function<P, V> valueProducer, BiFunction<P, V, P> valueModifier, BiConsumer<IPropertyProvider<P>, P> valueUser, Function<V, V> validator, IElementSerializer<V> serializer) {
		this(parent, name, defaultValue, valueProducer, (parentValue, value) -> valueUser.accept(parent, valueModifier.apply(parentValue, value)), validator, serializer);
	}

	public Property(IPropertyProvider<P> parent, String name, V defaultValue, Function<P, V> valueProducer, BiConsumer<P, V> valueConsumer, Function<V, V> validator, IElementSerializer<V> serializer) {
		this.parent = parent;
		this.name = name;
		this.defaultValue = defaultValue;
		this.valueProducer = valueProducer;
		this.valueConsumer = valueConsumer;
		this.validator = validator;
		this.serializer = serializer;
		if (serializer instanceof IPropertySerializer) {
			IPropertySerializer propertySerializer = (IPropertySerializer) serializer;
			for (IProperty property : propertySerializer.getProperties().getProperties(this)) {
				children.put(property.getName(), property);
			}
			collection = propertySerializer.getProperties();
		} else {
			collection = PropertyCollection.EMPTY;
		}
		this.cachedValue = getPropertyValue();
		if (serializer instanceof IPrimitiveSerializer) {
			IPrimitiveSerializer<V> primitiveSerializer = (IPrimitiveSerializer<V>) serializer;
			this.text = primitiveSerializer.toString(cachedValue);
		}
	}

	@Override
	public IPropertyProvider<P> getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public V getPropertyValue() {
		return valueProducer.apply(parent.getPropertyValue());
	}

	@Override
	public void setPropertyValue(V value) {
		setCacheValue(value);
		valueConsumer.accept(parent.getPropertyValue(), value);
	}

	@Override
	public V getCacheValue() {
		return cachedValue;
	}

	@Override
	public void setCacheValue(V value) {
		this.cachedValue = value;
		if (serializer instanceof IPrimitiveSerializer) {
			IPrimitiveSerializer<V> primitiveSerializer = (IPrimitiveSerializer<V>) serializer;
			this.text = primitiveSerializer.toString(value);
		}
	}

	@Override
	public String getText() {
		if (!cachedValue.equals(getPropertyValue())) {
			V value = getPropertyValue();
			if (serializer instanceof IPrimitiveSerializer) {
				IPrimitiveSerializer<V> primitiveSerializer = (IPrimitiveSerializer<V>) serializer;
				this.text = primitiveSerializer.toString(value);
			}
			this.cachedValue = value;
		}
		return text;
	}

	@Override
	public void setText(String text) {
		if (this.text.equals(text)) {
			return;
		}
		this.text = text;
		if (serializer instanceof IPrimitiveSerializer) {
			IPrimitiveSerializer<V> primitiveSerializer = (IPrimitiveSerializer<V>) serializer;
			setPropertyValue(primitiveSerializer.parseValue(text));
		} else if (serializer instanceof IPropertySerializer) {
			IPropertySerializer<V> propertySerializer = (IPropertySerializer<V>) serializer;
			setPropertyValue(propertySerializer.createValue(this));
		}
	}

	@Override
	public V validateValue(V value) {
		return validator.apply(value);
	}

	@Override
	public V getDefaultValue() {
		return defaultValue;
	}

	@Override
	public IElementSerializer<V> getSerializer() {
		return serializer;
	}

	@Override
	public boolean canInOrDecrease() {
		return serializer instanceof IRangeSerializer;
	}

	@Override
	public void increaseValue() {
		if (serializer instanceof IRangeSerializer) {
			IRangeSerializer<V> rangeSerializer = (IRangeSerializer<V>) serializer;
			setPropertyValue(rangeSerializer.getNextValue(getPropertyValue()));
		}
	}

	@Override
	public void decreaseValue() {
		if (serializer instanceof IRangeSerializer) {
			IRangeSerializer<V> rangeSerializer = (IRangeSerializer<V>) serializer;
			setPropertyValue(rangeSerializer.getPreviousValue(getPropertyValue()));
		}
	}

	@Override
	public <T> T getChildValue(Class<? extends T> valueClass, String name) {
		IProperty<T, ?> child = getChild(name);
		if (child == null) {
			throw new IllegalArgumentException("Failed to find child property (' " + name + "') of the property ('" + this.name + "')");
		}
		return child.getCacheValue();
	}

	@Override
	@Nullable
	public <T, U> IProperty<T, U> getChild(String name) {
		return children.get(name);
	}

	@Override
	public Collection<IProperty> getChildren() {
		return children.values();
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	@Override
	public IPropertyCollection getCollector() {
		return collection;
	}
}
