package leviathan.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.events.KeyEvent;
import leviathan.api.gui.events.MouseEvent;
import leviathan.gui.widget.Window;
import leviathan.utils.GuiUtil;
import org.lwjgl.input.Mouse;

public class GuiJEG extends GuiScreen implements IGuiSizable {
	protected final Window<GuiJEG> window;
	protected final int xSize;
	protected final int ySize;
	protected int guiLeft;
	protected int guiTop;

	public GuiJEG(int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.window = new Window<>(xSize, ySize, this);
		addElements();
	}

	protected void addElements() {
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		window.updateClient();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.setMousePosition(mouseX, mouseY);
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		window.draw(mouseX, mouseY);
		drawTooltips(mouseX, mouseY);
	}

	protected void drawTooltips(int mouseX, int mouseY) {
		InventoryPlayer playerInv = mc.player.inventory;

		if (playerInv.getItemStack().isEmpty()) {
			GuiUtil.drawTooltips(this, buttonList, mouseX, mouseY);
			GlStateManager.pushMatrix();
			GlStateManager.translate(guiLeft, guiTop, 0.0F);
			window.drawTooltip(mouseX, mouseY);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - xSize) / 2;
		this.guiTop = (this.height - ySize) / 2;
		window.init(guiLeft, guiTop);
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		window.setSize(width, height);
		super.setWorldAndResolution(mc, width, height);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);

			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		}
		IWidget origin = (window.getFocusedElement() == null) ? this.window : this.window.getFocusedElement();
		window.dispatchEvent(new KeyEvent(origin, KeyEvent.KEY_DOWN, typedChar, keyCode));
	}


	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		window.actionOnHovered(widget -> widget.hasListener(MouseEvent.MOUSE_DOWN), widget -> widget.dispatchEvent(new MouseEvent(widget, MouseEvent.MOUSE_DOWN, mouseX, mouseY, mouseButton)));
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		super.mouseReleased(mouseX, mouseY, mouseButton);

		window.actionOnHovered(widget -> widget.hasListener(MouseEvent.MOUSE_UP), widget -> widget.dispatchEvent(new MouseEvent(widget, MouseEvent.MOUSE_UP, mouseX, mouseY, mouseButton)));
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int dWheel = Mouse.getDWheel();
		if (dWheel != 0) {
			window.dispatchEvent(new MouseEvent.Wheel(window, dWheel));
		}
	}

	@Override
	public int getGuiLeft() {
		return guiLeft;
	}

	@Override
	public int getGuiTop() {
		return guiTop;
	}

	@Override
	public int getSizeX() {
		return xSize;
	}

	@Override
	public int getSizeY() {
		return ySize;
	}

	@Override
	public Minecraft getMC() {
		return mc;
	}
}
