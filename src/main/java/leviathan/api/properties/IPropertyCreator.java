package leviathan.api.properties;

public interface IPropertyCreator<P> {
	void addProperties(IPropertyCollector<P> collector);
}
