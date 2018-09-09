package leviathan.api.properties.serializer;

import java.util.function.Function;

import leviathan.api.properties.IProperty;
import leviathan.api.properties.IPropertyCollection;
import leviathan.api.properties.IPropertyCreator;
import leviathan.api.properties.PropertyCollection;

public class PropertySerializer<V> implements IPropertySerializer<V> {
	private final PropertyCollection collection;
	private final Function<IProperty<V, ?>, V> valueFactory;

	public PropertySerializer(Class<? extends V> valueClass, IPropertyCreator<V> creator, Function<IProperty<V, ?>, V> valueFactory) {
		this.collection = new PropertyCollection();
		this.valueFactory = valueFactory;
		collection.addProperties(valueClass, creator);
	}

	@Override
	public IPropertyCollection getProperties() {
		return collection;
	}

	@Override
	public V createValue(IProperty<V, ?> property) {
		return valueFactory.apply(property);
	}
}
