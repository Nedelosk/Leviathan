package leviathan.gui.widget.layouts;

import leviathan.api.gui.IWidget;

public class HorizontalLayout extends AbstractWidgetLayout {

	public HorizontalLayout(int xPos, int yPos, int height) {
		super(xPos, yPos, 0, height);
	}

	public <E extends IWidget> E add(E element) {
		elements.add(element);
		element.setParent(this);
		element.setXPosition(width);
		setWidth(width + (element.getWidth() + distance));
		return element;
	}

	public <E extends IWidget> E remove(E element) {
		elements.remove(element);
		setWidth(width - (element.getWidth() + distance));
		element.setXPosition(0);
		element.onDeletion();
		return element;
	}

	@Override
	public int getHeight() {
		if (height > 0) {
			return height;
		}
		int height = 0;
		for (IWidget element : elements) {
			int elementHeight = element.getHeight();
			if (elementHeight > height) {
				height = elementHeight;
			}
		}
		return height;
	}
}
