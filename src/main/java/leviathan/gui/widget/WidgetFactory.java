package leviathan.gui.widget;

import leviathan.api.gui.IWidgetContainer;
import leviathan.api.gui.IWidgetFactory;
import leviathan.api.text.ITextStyle;
import leviathan.api.text.TextStyleBuilder;
import leviathan.widgets.layouts.HorizontalLayout;
import leviathan.widgets.layouts.PaneLayout;
import leviathan.widgets.layouts.VerticalLayout;
import leviathan.utils.ColourProperties;

public class WidgetFactory implements IWidgetFactory {
	/* Constants */
	public static final ITextStyle DOMINANT_STYLE = new TextStyleBuilder().color(ColourProperties.INSTANCE.getSupplier("gui.beealyzer.dominant")).build();
	public static final ITextStyle RECESSIVE_STYLE = new TextStyleBuilder().color(ColourProperties.INSTANCE.getSupplier("gui.beealyzer.recessive")).build();
	public static final ITextStyle GUI_STYLE = new TextStyleBuilder().color(ColourProperties.INSTANCE.getSupplier("gui.screen")).build();
	public static final ITextStyle GUI_TITLE_STYLE = new TextStyleBuilder().color(ColourProperties.INSTANCE.getSupplier("gui.title")).build();

	/* Instance */
	public static final WidgetFactory INSTANCE = new WidgetFactory();

	private WidgetFactory() {
	}

	@Override
	public ITextStyle getGuiStyle() {
		return GUI_STYLE;
	}

	@Override
	public IWidgetContainer createVertical(int xPos, int yPos, int width, int gap) {
		return new WidgetContainer(xPos, yPos, width, 0).setLayout(new VerticalLayout(gap));
	}

	@Override
	public IWidgetContainer createHorizontal(int xPos, int yPos, int height, int gap) {
		return new WidgetContainer(xPos, yPos, 0, height).setLayout(new HorizontalLayout(gap));
	}

	@Override
	public IWidgetContainer createPane(int xPos, int yPos, int width, int height) {
		return new WidgetContainer(xPos, yPos, width, height).setLayout(PaneLayout.INSTANCE);
	}
}
