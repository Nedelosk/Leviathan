package leviathan.gui.widget.layouts;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetGroup;
import leviathan.api.gui.IWidgetLayout;
import leviathan.api.gui.IWidgetLayoutHelper;
import leviathan.api.gui.WidgetAlignment;

public class WidgetLayoutHelper implements IWidgetLayoutHelper {
	private final List<IWidgetLayout> layouts = new ArrayList<>();
	private final LayoutFactory layoutFactory;
	private final int width;
	private final int height;
	private final IWidgetGroup parent;
	private int xOffset;
	private int yOffset;
	@Nullable
	private IWidgetLayout currentLayout;
	private boolean horizontal;

	public WidgetLayoutHelper(LayoutFactory layoutFactory, int width, int height, IWidgetGroup parent) {
		this.layoutFactory = layoutFactory;
		this.width = width;
		this.height = height;
		this.parent = parent;
	}

	/**
	 * @return Only false if the helper has no space to add this element.
	 */
	@Override
	public boolean add(IWidget element) {
		if (currentLayout == null) {
			layouts.add(currentLayout = layoutFactory.createLayout(0, 0));
			this.horizontal = currentLayout instanceof VerticalLayout;
		}
		int groupWidth = currentLayout.getWidth();
		int groupHeight = currentLayout.getHeight();
		int eleWidth = element.getX() + element.getWidth();
		int eleHeight = element.getY() + element.getHeight();
		if (horizontal) {
			if (yOffset >= height) {
				if (width != 0 && xOffset > width) {
					return false;
				}
				xOffset += currentLayout.getWidth();
				layouts.add(currentLayout = layoutFactory.createLayout(0, 0));
				groupHeight = currentLayout.getHeight();
			}
			groupHeight += eleHeight;
			yOffset = groupHeight;
		} else {
			if (xOffset >= width) {
				if (height != 0 && yOffset > height) {
					return false;
				}
				yOffset += currentLayout.getHeight();
				layouts.add(currentLayout = layoutFactory.createLayout(0, 0));
				groupWidth = currentLayout.getWidth();
			}
			groupWidth += eleWidth;
			xOffset = groupWidth;
		}
		currentLayout.add(element);
		return true;
	}

	@Override
	public void finish(boolean center) {
		for (IWidget element : layouts) {
			if (center) {
				element.setAlign(WidgetAlignment.TOP_CENTER);
			}
			parent.add(element);
		}
		clear();
	}

	@Override
	public void clear() {
		layouts.clear();
		currentLayout = null;
		xOffset = 0;
		yOffset = 0;
	}

	@Override
	public Collection<IWidgetLayout> layouts() {
		return layouts;
	}
}
