package leviathan.gui.layouts;

import leviathan.api.ILayoutManager;
import leviathan.api.Region;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetContainer;

public class VerticalLayout implements ILayoutManager {

	/**
	 * This is the gap (in pixels) between the elements of the container.
	 */
	private int gap;

	public VerticalLayout(int gap) {
		this.gap = gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	public int getGap() {
		return gap;
	}

	@Override
	public void layoutWidget(IWidgetContainer layout) {
		int height = 0;
		for(IWidget widget : layout){
			if(!widget.isVisible()){
				continue;
			}
			Region widgetRegion = widget.getRegion();
			widget.setRegion(widgetRegion.withPosition(widgetRegion.getX(), height));
			height+=widgetRegion.getHeight() + gap;
		}
		layout.setHeight(height);
	}
}
