package leviathan.api.properties;

public interface IPropertyProvider<V> {
	IPropertyCollection getCollector();

	V getPropertyValue();

	void setPropertyValue(V value);
}
