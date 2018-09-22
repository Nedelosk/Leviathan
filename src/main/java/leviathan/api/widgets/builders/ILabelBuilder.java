package leviathan.api.widgets.builders;

import leviathan.api.geometry.VerticalAlignment;
import leviathan.api.geometry.HorizontalAlignment;
import leviathan.api.text.ITextStyle;
import leviathan.api.widgets.ILabelWidget;

public interface ILabelBuilder extends IWidgetBuilder<ILabelWidget, ILabelBuilder> {
	ILabelBuilder setText(String text);

	ILabelBuilder setAutoSize(boolean autoSize);

	ILabelBuilder setStyle(ITextStyle textStyle);

	ILabelBuilder setHorizontal(HorizontalAlignment alignment);

	ILabelBuilder setVertical(VerticalAlignment alignment);
}
