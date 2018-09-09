package leviathan.gui.widget;

import java.awt.Color;

import leviathan.api.gui.IColoredWidget;
import leviathan.api.properties.IPropertyCollection;
import leviathan.gui.properties.JEGSerializers;

public class ColoredWidget extends Widget implements IColoredWidget {
	private int color = 0xFFFFFFFF;

	public ColoredWidget(int xPos, int yPos, int width, int height, int color) {
		super(xPos, yPos, width, height);
		this.color = color;
	}

	@Override
	public void addProperties(IPropertyCollection collection) {
		super.addProperties(collection);
		collection.addProperties(ColoredWidget.class, creator -> creator.add("color", Color.WHITE, ColoredWidget::getColor, ColoredWidget::setColor, JEGSerializers.COLOR));
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		super.drawElement(mouseX, mouseY);
		drawRect(0, 0, getWidth(), getHeight(), color);
	}

	@Override
	public int getRGB() {
		return color;
	}

	@Override
	public void setColor(int rgb) {
		color = rgb;
	}
}
