package leviathan.api.gui;

public interface IScrollable {

	void onScroll(int value);

	/**
	 * @param mouseX the mouse x position relative to the screen
	 * @param mouseY the mouse y position relative to the screen
	 */
	boolean isFocused(int mouseX, int mouseY);
}
