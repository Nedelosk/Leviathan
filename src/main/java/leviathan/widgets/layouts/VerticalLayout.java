package leviathan.widgets.layouts;

import leviathan.api.ILayoutManager;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IWidget;

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
	public void layoutWidget(IContainer layout) {
		int height = 0;
		for(IWidget widget : layout){
			if(!widget.isVisible()){
				continue;
			}
			/*Region widgetRegion = widget.getRegion();
			widget.setRegion(widgetRegion.withPosition(widgetRegion.getX(), height));
			height+=widgetRegion.getHeight() + gap;*/
			widget.setY(height);
			height += widget.getHeight() + gap;
		}
		layout.setHeight(height);
	}
}
