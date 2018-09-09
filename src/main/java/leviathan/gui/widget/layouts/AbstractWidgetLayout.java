package leviathan.gui.widget.layouts;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.IWidgetLayout;

@SideOnly(Side.CLIENT)
public abstract class AbstractWidgetLayout extends WidgetGroup implements IWidgetLayout {
	/* The distance between the different elements of this group. */
	public int distance;

	public AbstractWidgetLayout(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}

	public AbstractWidgetLayout setDistance(int distance) {
		this.distance = distance;
		return this;
	}

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public int getSize() {
		return elements.size();
	}
}
