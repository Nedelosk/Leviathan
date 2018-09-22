package leviathan.api.gui;

import java.util.function.Supplier;

import net.minecraftforge.registries.IForgeRegistryEntry;

import leviathan.api.WidgetTab;
import leviathan.api.render.DrawMode;
import leviathan.gui.widget.TextureWidget;
import leviathan.gui.widget.Widget;
import leviathan.gui.GuiWorkspace;
import leviathan.utils.Drawable;

public class WidgetType extends IForgeRegistryEntry.Impl<IWidgetType> implements IWidgetType {
	//TODO: Create fake widget.
	public static final WidgetType EMPTY = new WidgetType("empty", WidgetTab.INVISIBLE, () -> new Widget(0, 0, 0, 0));
	public static final WidgetType BACKGROUND = new WidgetType("background", WidgetTab.OBJECTS, () -> new TextureWidget(0, 0, 32, 32, new Drawable(DrawMode.SLICED, GuiWorkspace.BACKGROUND_TEXTURE)));

	private final WidgetTab tab;
	private final Supplier<IWidget> widgetFactory;
	private final boolean container;

	public WidgetType(String name, WidgetTab tab, Supplier<IWidget> widgetFactory) {
		this(name, tab, widgetFactory, false);
	}

	public WidgetType(String name, WidgetTab tab, Supplier<IWidget> widgetFactory, boolean container) {
		setRegistryName(name);
		this.tab = tab;
		this.widgetFactory = widgetFactory;
		this.container = container;
	}

	public WidgetTab getTab() {
		return tab;
	}

	@Override
	public IWidget createWidget() {
		return widgetFactory.get();
	}

	@Override
	public boolean isContainer() {
		return container;
	}
}
