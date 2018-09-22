package leviathan.api.widgets;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import leviathan.api.geometry.Vector;
import leviathan.api.tooltip.ITooltipSupplier;

public interface ITooltipProvider {
	boolean hasTooltip();

	void clearTooltip();

	default void addTooltip(String tooltip){
		addTooltip((lines, element, mousePosition) -> lines.add(tooltip));
	}

	default void addTooltip(Collection<String> tooltip){
		addTooltip((lines, element, mousePosition) -> lines.addAll(tooltip));
	}

	default void addTooltip(String... tooltip){
		addTooltip((lines, element, mousePosition) -> lines.addAll(Arrays.asList(tooltip)));
	}

	void addTooltip(ITooltipSupplier supplier);

	List<String> getTooltip(Vector mousePosition);
}
