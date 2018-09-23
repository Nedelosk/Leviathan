package leviathan.widgets.layouts;

import leviathan.api.ILayoutManager;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IWidget;

public class HorizontalLayout implements ILayoutManager {

	/**
	 * This is the gap (in pixels) between the elements of the container.
	 */
	private int gap;

	public HorizontalLayout(int gap) {
		this.gap = gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	public int getGap() {
		return gap;
	}

	@Override
	public void layoutWidget(IContainer layout) {
		int width = 0;
		for(IWidget widget : layout){
			if(!widget.isVisible()){
				continue;
			}
			//Region widgetRegion = widget.getRegion();
			widget.setX(width);
			//widget.setRegion(widgetRegion.withPosition(width, widgetRegion.getY()));
			width += widget.getWidth() + gap;
		}
		layout.setWidth(width);
		//layout.setWidth(width);
	}
}
