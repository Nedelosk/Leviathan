package leviathan.api.gui;

import net.minecraftforge.registries.IForgeRegistryEntry;

import leviathan.api.WidgetTab;

public interface IWidgetType extends IForgeRegistryEntry<IWidgetType> {
	IWidget createWidget();

	boolean isContainer();

	WidgetTab getTab();
}
