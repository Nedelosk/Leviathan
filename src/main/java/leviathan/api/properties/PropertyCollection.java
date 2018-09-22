package leviathan.api.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PropertyCollection implements IPropertyCollection {
	public static final Empty EMPTY = new Empty();
	private final List<IPropertyCreator> creators = new ArrayList<>();

	@Override
	public <P> void addProperties(Class<? extends P> providerClass, IPropertyCreator<P> creator) {
		creators.add(creator);
	}

	@Override
	public <T, P extends IPropertyProvider<T>> Collection<IProperty<?, T>> getProperties(P provider) {
		PropertyCollector<T> collector = new PropertyCollector<>(provider);
		creators.forEach(creator -> creator.addProperties(collector));

		return collector.properties;
	}

	private static final class Empty implements IPropertyCollection {

		private Empty() {
		}

		@Override
		public <P> void addProperties(Class<? extends P> providerClass, IPropertyCreator<P> creator) {
		}

		@Override
		public <T, P extends IPropertyProvider<T>> Collection<IProperty<?, T>> getProperties(P provider) {
			return Collections.emptyList();
		}
	}
}
