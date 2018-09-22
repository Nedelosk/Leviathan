package leviathan.api.tooltip;

public interface ITooltipProvider {
	boolean isRelativeToGui();

	boolean isToolTipVisible();

	Tooltip getToolTip(int mX, int mY);

	boolean isMouseOver(int mX, int mY);
}
