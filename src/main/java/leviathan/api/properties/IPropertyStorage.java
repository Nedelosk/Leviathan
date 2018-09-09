package leviathan.api.properties;

public interface IPropertyStorage {

	<V, P extends IPropertyProvider> IProperty<V, P> getProperty(String path);

	<V, P extends IPropertyProvider> V getValue(IProperty<V, P> property, P provider);

	<V, P extends IPropertyProvider> void setValue(IProperty<V, P> property, P provider, V value);
}
