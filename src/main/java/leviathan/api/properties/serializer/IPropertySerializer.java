package leviathan.api.properties.serializer;

import leviathan.api.properties.IProperty;
import leviathan.api.properties.IPropertyCollection;

public interface IPropertySerializer<V> extends IElementSerializer<V> {
	IPropertyCollection getProperties();

	V createValue(IProperty<V, ?> property);

}
