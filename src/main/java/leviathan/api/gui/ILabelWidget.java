package leviathan.api.gui;

import leviathan.api.gui.style.ITextStyle;

/**
 * A label element that displays and contains one single line of text
 */
public interface ILabelWidget extends ITextWidget {
	/**
	 * @return The style of the text.
	 */
	ITextStyle getStyle();

	ILabelWidget setStyle(ITextStyle style);

	/**
	 * @return The current text of this element with its {@link net.minecraft.util.text.TextFormatting}s.
	 */
	String getText();

	ILabelWidget setText(String text);

	/**
	 * The current text of this element without its {@link net.minecraft.util.text.TextFormatting}s.
	 */
	String getRawText();
}
