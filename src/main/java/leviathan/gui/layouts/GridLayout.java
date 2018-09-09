package leviathan.gui.layouts;

import leviathan.api.ILayoutManager;
import leviathan.api.gui.IWidgetContainer;

public class GridLayout implements ILayoutManager {
	private int columns;
	private int rows;
	private int horizontalGap;
	private int verticalGap;

	public GridLayout(int columns, int rows) {
		this(columns, rows, 0, 0);
	}

	public GridLayout(int columns, int rows, int horizontalGap, int verticalGap) {
		this.columns = columns;
		this.rows = rows;
		this.horizontalGap = horizontalGap;
		this.verticalGap = verticalGap;
	}

	@Override
	public void layoutWidget(IWidgetContainer layout) {

	}
}
