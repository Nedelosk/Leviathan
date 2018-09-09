package leviathan.gui.workspace.widgets;

import leviathan.gui.widget.layouts.WidgetGroup;
import leviathan.gui.workspace.GuiWorkspace;

public class WorkspaceControl extends WidgetGroup {
	protected final GuiWorkspace creator;

	public WorkspaceControl(GuiWorkspace creator) {
		super(0, 0, 90, 32);
		this.creator = creator;
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		drawGradientRect(0, 0, getWidth(), getHeight(), 0xFF545454, 0xFF4c4c4c);
		drawHorizontalLine(0, getWidth() - 1, 0, -2039584);
		drawHorizontalLine(0, getWidth() - 1, getHeight() - 1, -6250336);
		drawVerticalLine(0, 0, getHeight() - 1, -2039584);
		drawVerticalLine(getWidth() - 1, 0, getHeight() - 1, -6250336);
		super.drawElement(mouseX, mouseY);
	}
}
