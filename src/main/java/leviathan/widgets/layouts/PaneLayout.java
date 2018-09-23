package leviathan.widgets.layouts;

import leviathan.api.ILayoutManager;
import leviathan.api.geometry.Point;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IWidget;

public class PaneLayout implements ILayoutManager {
	public static final PaneLayout INSTANCE = new PaneLayout();

	private PaneLayout() {
	}

	@Override
	public void layoutWidget(IContainer layout) {
		Point size = Point.ORIGIN;
		for (IWidget widget : layout.getChildren()) {
			int wWidth = widget.getWidth() + widget.getX();
			int wHeight = widget.getHeight() + widget.getY();
			if (wWidth > size.x) {
				size = size.withX(wWidth);
			}
			if (wHeight > size.y) {
				size = size.withY(wHeight);
			}
		}
		layout.setSize(size);
	}
}
