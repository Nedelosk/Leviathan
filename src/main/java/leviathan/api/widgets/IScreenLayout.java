package leviathan.api.widgets;

import java.util.Collection;

@FunctionalInterface
public interface IScreenLayout {
	void layoutWindows(IScreen screen, Collection<IWindow> windows);
}
