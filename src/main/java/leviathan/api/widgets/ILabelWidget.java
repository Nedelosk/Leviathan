package leviathan.api.widgets;

import leviathan.api.geometry.HorizontalAlignment;
import leviathan.api.geometry.VerticalAlignment;
import leviathan.api.text.ITextStyle;

public interface ILabelWidget extends IWidget {
	String getFormattedText();

	String getText();

	void setText(String text);

	ITextStyle getTextStyle();

	void setTextStyle(ITextStyle textStyle);

	HorizontalAlignment getHorizontalAlignment();

	void setHorizontalAlignment(HorizontalAlignment horizontalAlignment);

	VerticalAlignment getVerticalAlignment();

	void setVerticalAlignment(VerticalAlignment verticalAlignment);

	boolean isAutoSize();

	void setAutoSize(boolean autoSize);
}
