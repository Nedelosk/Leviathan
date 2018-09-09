package leviathan.gui.widget;

import leviathan.api.gui.IWidgetFactory;
import leviathan.api.gui.style.ITextStyle;
import leviathan.api.gui.style.TextStyleBuilder;
import leviathan.gui.widget.layouts.AbstractWidgetLayout;
import leviathan.gui.widget.layouts.HorizontalLayout;
import leviathan.gui.widget.layouts.PaneLayout;
import leviathan.gui.widget.layouts.VerticalLayout;
import leviathan.gui.widget.layouts.WidgetGroup;
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
	public AbstractWidgetLayout createVertical(int xPos, int yPos, int width) {
		return new VerticalLayout(xPos, yPos, width);
	}

	@Override
	public AbstractWidgetLayout createHorizontal(int xPos, int yPos, int height) {
		return new HorizontalLayout(xPos, yPos, height);
	}

	@Override
	public WidgetGroup createPane(int xPos, int yPos, int width, int height) {
		return new PaneLayout(xPos, yPos, width, height);
	}
}
