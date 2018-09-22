package leviathan.widgets.builders;

import leviathan.api.geometry.HorizontalAlignment;
import leviathan.api.geometry.VerticalAlignment;
import leviathan.api.text.ITextStyle;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.ILabelWidget;
import leviathan.api.widgets.builders.ILabelBuilder;
import leviathan.widgets.LabelWidget;

public class LabelBuilder extends WidgetBuilder<ILabelWidget, ILabelBuilder> implements ILabelBuilder {

	private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
	private VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
	private ITextStyle textStyle;
	private boolean autoSize = true;
	private String text;

	public LabelBuilder(IContainer container, String name, String text, ITextStyle textStyle) {
		super(container, name);
		this.text = text;
		this.textStyle = textStyle;
	}

	@Override
	public ILabelBuilder setText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public ILabelBuilder setAutoSize(boolean autoSize) {
		this.autoSize = autoSize;
		return this;
	}

	@Override
	public ILabelBuilder setStyle(ITextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}

	@Override
	public ILabelBuilder setHorizontal(HorizontalAlignment alignment) {
		this.horizontalAlignment = alignment;
		return this;
	}

	@Override
	public ILabelBuilder setVertical(VerticalAlignment alignment) {
		this.verticalAlignment = alignment;
		return this;
	}

	@Override
	public ILabelWidget create() {
		return LabelWidget.create(name, x, y, width, height, text, textStyle, horizontalAlignment, verticalAlignment, autoSize);
	}
}
