package leviathan.gui.layouts;

import leviathan.api.ILayoutManager;
import leviathan.api.Region;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetContainer;

public class PaneLayout implements ILayoutManager {
	public static final PaneLayout INSTANCE = new PaneLayout();

	private PaneLayout() {
	}

	@Override
	public void layoutWidget(IWidgetContainer layout) {
		int width = 0;
		int height = 0;
		for(IWidget widget : layout.getElements()){
			Region widgetRegion = widget.getRegion();
			int wWidth = widgetRegion.getWidth() + widgetRegion.getX();
			int wHeight = widgetRegion.getHeight() + widgetRegion.getY();
			if(wWidth > width){
				width = wWidth;
			}
			if(wHeight > height){
				height = wHeight;
			}
		}
		layout.setSize(width, height);
	}
}
