package leviathan.api.gui;

import java.util.Collection;
import java.util.Map;

import leviathan.api.text.ITextStyle;

/**
 * A element that displays or contains one ore more lines of text.
 */
public interface ITextWidget extends IWidget {

	/**
	 * @return The text this element displays.
	 */
	Collection<String> getLines();

	ITextWidget setText(String text);

	/**
	 * @return The raw text this element displays without any formations and their style.
	 */
	Map<ITextStyle, String> getRawLines();
}
