package leviathan.api.gui;

import java.awt.Color;

public interface IColoredWidget extends IWidget {

	int getRGB();

	void setColor(int rgb);

	default Color getColor() {
		return new Color(getRGB());
	}

	default void setColor(Color color) {
		setColor(color.getRGB());
	}

}
