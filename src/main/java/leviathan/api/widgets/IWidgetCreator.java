package leviathan.api.widgets;

import leviathan.api.widgets.builders.ILabelBuilder;

public interface IWidgetCreator {

	ILabelBuilder label(String name, String text);


}
