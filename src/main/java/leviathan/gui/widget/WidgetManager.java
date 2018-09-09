package leviathan.gui.widget;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;

import leviathan.api.gui.IWidget;
import leviathan.gui.IGuiSizable;
import leviathan.gui.widget.layouts.PaneLayout;
import leviathan.gui.widget.layouts.WidgetGroup;

public class WidgetManager<G extends GuiScreen & IGuiSizable> {
	/* Attributes - Final */
	private final WidgetGroup container;

	public WidgetManager(G gui) {
		this.container = new PaneLayout(0, 0);
	}

	public WidgetGroup group() {
		return container;
	}

	public void add(IWidget element) {
		container.add(element);
	}

	public void remove(IWidget element) {
		container.remove(element);
	}

	public void clear() {
		container.clear();
	}

	public void draw(int mouseX, int mouseY) {
		container.draw(mouseX, mouseY);
	}

	public void init(int guiLeft, int guiTop) {
		container.setLocation(guiLeft, guiTop);
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	public void mouseClickMove(int mouseX, int mouseY, int mouseButton) {
	}

	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}

	public boolean keyTyped(char typedChar, int keyCode) {
		return false;
	}

	public void drawTooltip(int mouseX, int mouseY) {
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		int mX = mouseX - container.getX();
		int mY = mouseY - container.getY();
		return getElements().stream().anyMatch(element -> element.isMouseOver(mX, mY));
	}

	public List<String> getTooltip(int mouseX, int mouseY) {
		return container.getTooltip(mouseX, mouseY);
	}

	public List<IWidget> getElements() {
		return container.getElements();
	}
}
