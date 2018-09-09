package leviathan.gui.widget;

import javax.annotation.Nullable;
import java.util.ArrayList;

import leviathan.api.Region;
import leviathan.api.gui.IScrollable;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.events.MouseEvent;

public class ScrollableWidget extends WidgetContainer implements IScrollable {
	@Nullable
	private IWidget content;
	private float scrollPercentage;
	private float step;

	public ScrollableWidget(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
		addListener(MouseEvent.WHEEL, event -> {
			//movePercentage(event.getDWheel());
		});
	}

	public int getInvisibleArea() {
		step = (12 * 0.5F);
		if (content == null) {
			return 0;
		}
		return (int) ((content.getHeight() - getHeight()) / (step));
	}

	protected void movePercentage(float percentage) {
		scrollPercentage = (percentage * step);
	}

	@Override
	public void onScroll(int value) {
		scrollPercentage = (value * step);
		if (content != null) {
			content.setYPosition(-((int) scrollPercentage));
		}
	}

	public void setContent(@Nullable IWidget content) {
		this.content = content;
		if (content != null) {
			content.setCroppedZone(this, new Region(0, 0, getWidth(), getHeight()));
		}
	}

	@Override
	protected void onRegionChange(Region oldRegion, Region newRegion) {
		super.onRegionChange(oldRegion, newRegion);
		if (content != null) {
			content.setCroppedZone(this, new Region(0, 0, newRegion.getWidth(), newRegion.getHeight()));
		}
	}

	@Override
	public void clear() {
		remove(new ArrayList<>(elements));
	}

	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return isMouseOver();
	}

	@Override
	public boolean canMouseOver() {
		return false;
	}
}
