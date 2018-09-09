package leviathan.api.properties;

import javax.annotation.Nullable;
import java.util.Collection;

import leviathan.api.properties.serializer.IElementSerializer;

public interface IProperty<V, P> extends IPropertyProvider<V> {

	String getName();

	IPropertyProvider<P> getParent();

	String getText();

	void setText(String text);

	V validateValue(V value);

	V getDefaultValue();

	V getCacheValue();

	void setCacheValue(V value);

	boolean canInOrDecrease();

	void increaseValue();

	void decreaseValue();

	IElementSerializer<V> getSerializer();

	<T> T getChildValue(Class<? extends T> valueClass, String name);

	@Nullable
	<T, U> IProperty<T, U> getChild(String name);

	Collection<IProperty> getChildren();

	boolean hasChildren();
}
