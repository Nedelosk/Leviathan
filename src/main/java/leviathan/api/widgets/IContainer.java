package leviathan.api.widgets;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import leviathan.api.ILayoutManager;

public interface IContainer extends IWidget, Iterable<IWidget>, IWidgetCreator {

	default void add(IWidget widget){
		add(Collections.singletonList(widget));
	}

	default void add(IWidget... widgets){
		add(Arrays.asList(widgets));
	}

	void add(Collection<IWidget> widgets);

	default void remove(IWidget widget){
		remove(Collections.singletonList(widget));
	}

	default void remove(IWidget... widgets){
		remove(Arrays.asList(widgets));
	}

	void remove(Collection<IWidget> widgets);

	default void clear(){
		remove(new ArrayList<>(getChildren()));
	}

	Collection<IWidget> getChildren();

	void setLayoutManager(ILayoutManager layoutManager);

	ILayoutManager getLayoutManager();

	@Override
	@Nonnull
	default Iterator<IWidget> iterator() {
		return getChildren().iterator();
	}
}
