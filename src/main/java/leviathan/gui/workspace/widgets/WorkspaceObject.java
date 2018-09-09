package leviathan.gui.workspace.widgets;

import net.minecraft.client.renderer.GlStateManager;

import leviathan.api.Region;
import leviathan.api.gui.IWidget;
import leviathan.gui.widget.layouts.WidgetGroup;
import leviathan.gui.workspace.GuiWorkspace;

public class WorkspaceObject extends WidgetGroup {
	public WorkspaceObject() {
		super(0, 0, 176, 166);
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		GlStateManager.enableAlpha();
		GuiWorkspace.BACKGROUND.draw(0, 0, getWidth(), getHeight());
		GlStateManager.disableAlpha();
		super.drawElement(mouseX, mouseY);
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}

	@Override
	public boolean canFocus() {
		return true;
	}

	@Override
	public IWidget setRegion(Region region) {
		if (parent == null) {
			return super.setRegion(region);
		}
		Region parentRegion = parent.getRegion();
		Region oldRegion = this.region;
		int xPos = region.getX();
		int yPos = region.getY();
		int width = region.getWidth();
		int height = region.getHeight();
		int distRight = parentRegion.getWidth() - (xPos + width);
		int distBottom = parentRegion.getHeight() - (yPos + height);
		if (xPos < 0) {
			xPos = 0;
			if (width != oldRegion.getWidth()) {
				width = oldRegion.getWidth();
			}
		}
		if (yPos < 0) {
			yPos = 0;
			if (height != oldRegion.getHeight()) {
				height = oldRegion.getHeight();
			}
		}
		if (distRight < 0) {
			if (width != oldRegion.getWidth()) {
				width = oldRegion.getWidth();
			}
			xPos = parentRegion.getWidth() - width;
		}
		if (distBottom < 0) {
			if (height != oldRegion.getHeight()) {
				height = oldRegion.getHeight();
			}
			yPos = parentRegion.getHeight() - height;
		}
		return super.setRegion(new Region(xPos, yPos, width, height));
	}
}
