package leviathan.api.properties;

import java.util.Collection;

public interface IPropertyCollection {
	<P> void addProperties(Class<? extends P> providerClass, IPropertyCreator<P> creator);

	<T, P extends IPropertyProvider<T>> Collection<IProperty<?, T>> getProperties(P provider);
}
