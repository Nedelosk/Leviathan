package leviathan.api.gui.tooltip;

public interface ITooltipProvider {
	boolean isRelativeToGui();

	boolean isToolTipVisible();

	Tooltip getToolTip(int mX, int mY);

	boolean isMouseOver(int mX, int mY);
}
