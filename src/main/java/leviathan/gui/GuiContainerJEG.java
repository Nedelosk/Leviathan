package leviathan.gui;

import javax.annotation.Nullable;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.events.KeyEvent;
import leviathan.api.gui.events.MouseEvent;
import leviathan.gui.widget.Window;
import leviathan.utils.ColourProperties;
import leviathan.utils.GuiUtil;
import org.lwjgl.input.Mouse;

public class GuiContainerJEG extends GuiContainer implements IGuiSizable {
	private final Window<GuiContainerJEG> window;

	public GuiContainerJEG(Container inventorySlotsIn) {
		super(inventorySlotsIn);

		this.window = new Window<>(xSize, ySize, this);
	}

	@Override
	public void initGui() {
		super.initGui();
		this.window.init(guiLeft, guiTop);
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		window.setSize(width, height);
		super.setWorldAndResolution(mc, width, height);
	}

	@Override
	public void updateScreen() {
		window.updateClient();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.setMousePosition(mouseX, mouseY);
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public ColourProperties getFontColor() {
		return ColourProperties.INSTANCE;
	}

	public FontRenderer getFontRenderer() {
		return fontRenderer;
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
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode() && this.window.getFocusedElement() == null)) {
			this.mc.player.closeScreen();
		}
		IWidget origin = (window.getFocusedElement() == null) ? this.window : this.window.getFocusedElement();
		window.dispatchEvent(new KeyEvent(origin, KeyEvent.KEY_DOWN, typedChar, keyCode));
	}

	@Nullable
	protected Slot getSlotAtPosition(int mouseX, int mouseY) {
		for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
			Slot slot = this.inventorySlots.inventorySlots.get(k);

			if (isMouseOverSlot(slot, mouseX, mouseY)) {
				return slot;
			}
		}

		return null;
	}

	private boolean isMouseOverSlot(Slot par1Slot, int mouseX, int mouseY) {
		return isPointInRegion(par1Slot.xPos, par1Slot.yPos, 16, 16, mouseX, mouseY);
	}

	@Override
	protected boolean hasClickedOutside(int mouseX, int mouseY, int guiLeft, int guiTop) {
		return !window.isMouseOver(mouseX, mouseY) && super.hasClickedOutside(mouseX, mouseY, guiLeft, guiTop);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		InventoryPlayer playerInv = mc.player.inventory;

		if (playerInv.getItemStack().isEmpty()) {
			GuiUtil.drawTooltips(this, buttonList, mouseX, mouseY);
			GuiUtil.drawTooltips(this, inventorySlots.inventorySlots, mouseX, mouseY);
			window.drawTooltip(mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		window.draw(mouseX, mouseY);
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
