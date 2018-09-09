package leviathan.gui.workspace;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import leviathan.api.Region;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetContainer;
import leviathan.api.gui.IWidgetType;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.gui.WidgetType;
import leviathan.api.gui.events.ElementEvent;
import leviathan.api.gui.events.KeyEvent;
import leviathan.api.gui.events.MouseEvent;
import leviathan.api.render.ISpriteRenderer;
import leviathan.gui.IGuiSizable;
import leviathan.gui.widget.Window;
import leviathan.gui.workspace.widgets.WorkspaceCanvas;
import leviathan.gui.workspace.widgets.WorkspaceCreator;
import leviathan.gui.workspace.widgets.WorkspaceInspector;
import leviathan.gui.workspace.widgets.WorkspaceOverlay;
import leviathan.gui.workspace.widgets.WorkspaceTree;
import leviathan.utils.GuiUtil;
import leviathan.utils.RendererSliced;
import leviathan.utils.ResourceUtil;
import leviathan.utils.Sprite;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiWorkspace extends GuiScreen implements IGuiSizable, ITreeListListener {
	public static final Sprite BACKGROUND_TEXTURE = new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 0, 0, 64, 64);
	public static final ISpriteRenderer BACKGROUND = new RendererSliced(BACKGROUND_TEXTURE, 16, 16, 16, 16);

	protected final Window<GuiWorkspace> window;
	//
	public final WorkspaceCanvas canvas;
	//public final WorkspaceObject object;
	//Left SIde
	public final WorkspaceInspector inspector;
	//Right Side
	public final WorkspaceCreator creator;
	public final WorkspaceTree tree;

	public final WidgetTreeList treeList;


	private WorkspaceOverlay overlay;

	public GuiWorkspace() {
		this.window = new Window<>(255, 255, this);
		canvas = window.add(new WorkspaceCanvas());

		//object = new WorkspaceObject();
		//canvas.addObject(object);
		overlay = new WorkspaceOverlay(this);
		window.add(overlay);

		inspector = window.add(new WorkspaceInspector(this));
		creator = window.add(new WorkspaceCreator(this));
		tree = window.add(new WorkspaceTree(this));
		creator.setAlign(WidgetAlignment.BOTTOM_RIGHT);
		tree.setAlign(WidgetAlignment.TOP_RIGHT);

		//tree.addObject(object);

		treeList = new WidgetTreeList(canvas);
		treeList.addListener(this, inspector, tree);
		WidgetTreeEntry background = treeList.createEntry(WidgetType.BACKGROUND, true);
		Optional<IWidget> backgroundWidget = background.getWidget();
		backgroundWidget.ifPresent(widget -> widget.setSize(176, 166));

		canvas.addListener(MouseEvent.MOUSE_DOWN, event -> {
			if (creator.hasSelectedType()) {
				IWidgetType type = creator.getSelectedType();
				if (type != null) {
					treeList.createEntry(type, true);
					creator.deselectType();
					return;
				}
			}
			IWidget widgetAtTop = canvas.calculateMousedOverElement();
			if (widgetAtTop == null || widgetAtTop == event.getSource() && treeList.isRootSelected()) {
				return;
			}
			treeList.selectEntry(widgetAtTop.getName());
		});
		overlay.addListener(MouseEvent.MOUSE_DRAG_MOVE, event -> {
			IWidget origin = event.getSource();
			Optional<IWidget> optionalSelected = treeList.getSelectedWidget();
			if (!overlay.isVisible() || !optionalSelected.isPresent()) {
				return;
			}
			IWidget selectedWidget = optionalSelected.get();
			/*selectedWidget.setLocation(Math.round(selectedWidget.getX() + event.getDiffX()), Math.round(selectedWidget.getY() + event.getDiffY()));
			overlay.setRegion(selectedWidget.getRegion());*/
			setSelectedRegion(selectedWidget, selectedWidget.getRegion().withPosition(Math.round(selectedWidget.getX() + event.getDiffX()), Math.round(selectedWidget.getY() + event.getDiffY())));
		});
		window.addListener(ElementEvent.RENAME, event -> {
			if (event.isCanceled()) {
				return;
			}
			event.setCanceled(!treeList.renameEntry(event.getOldName(), event.getNewName()));
		});
		window.addListener(KeyEvent.KEY_DOWN, event -> {
			int code = event.getKey();
			if (code == Keyboard.KEY_BACKSLASH || code == Keyboard.KEY_DELETE) {
				treeList.deleteEntry(null);
			}
		});
	}

	public void setSelectedRegion(IWidget selectedWidget, Region region) {
		Region canvasRegion = canvas.getRegion();
		Region oldRegion = selectedWidget.getRegion();
		int xPos = region.getX();
		int yPos = region.getY();
		int width = region.getWidth();
		int height = region.getHeight();
		int distRight = canvasRegion.getWidth() - (xPos + width);
		int distBottom = canvasRegion.getHeight() - (yPos + height);
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
			xPos = canvasRegion.getWidth() - width;
		}
		if (distBottom < 0) {
			if (height != oldRegion.getHeight()) {
				height = oldRegion.getHeight();
			}
			yPos = canvasRegion.getHeight() - height;
		}
		selectedWidget.setRegion(new Region(xPos, yPos, width, height));
		IWidget parent = selectedWidget.getParent();
		if (parent == null) {
			parent = canvas;
		}
		overlay.setRegion(selectedWidget.getRegion().translate(parent.getAbsoluteX(), parent.getAbsoluteY()));
	}

	@Override
	public void onEntrySelection(WidgetTreeList treeList, WidgetTreeEntry entry, @Nullable WidgetTreeEntry oldEntry) {
		Optional<IWidget> optionalWidget = entry.getWidget();
		if (entry.isRoot() || !optionalWidget.isPresent()) {
			overlay.setVisible(false);
			return;
		}
		IWidget widget = optionalWidget.get();
		overlay.setVisible(true);
		IWidget parent = widget.getParent();
		if (parent == null) {
			parent = canvas;
		}
		overlay.setRegion(widget.getRegion().translate(parent.getAbsoluteX(), parent.getAbsoluteY()));
	}

	@Override
	public void onEntryCreation(WidgetTreeList treeList, WidgetTreeEntry entry) {
		IWidget widget = entry.type.createWidget();
		widget.setName(entry.getName());
		canvas.add(widget);
	}

	@Override
	public void onEntryRename(WidgetTreeList treeList, WidgetTreeEntry entry, String oldName, String newName) {
		/*Optional<IWidget> optionalWidget = entry.getWidget();
		if(!optionalWidget.isPresent()){
			return;
		}
		IWidget widget = optionalWidget.get();
		widget.setName(newName);*/
	}

	@Override
	public void onEntryTransfer(WidgetTreeList treeList, WidgetTreeEntry entry, WidgetTreeEntry oldParent, WidgetTreeEntry newParent) {
		Optional<IWidget> optionalWidget = entry.getWidget();
		Optional<IWidget> oldParentWidget = oldParent.getWidget();
		Optional<IWidget> newParentWidget = newParent.getWidget();
		if (!optionalWidget.isPresent() || !oldParentWidget.isPresent() || !newParentWidget.isPresent()) {
			return;
		}
		IWidget widget = optionalWidget.get();
		IWidget oldP = oldParentWidget.get();
		IWidget newP = newParentWidget.get();
		if (!(newP instanceof IWidgetContainer) || !(oldP instanceof IWidgetContainer)) {
			return;
		}
		((IWidgetContainer) oldP).remove(widget);
		((IWidgetContainer) newP).add(widget);
	}

	@Override
	public void onEntryDeletion(WidgetTreeList treeList, WidgetTreeEntry entry) {
		Optional<IWidget> optionalWidget = entry.getWidget();
		if (!optionalWidget.isPresent()) {
			return;
		}
		IWidget widget = optionalWidget.get();
		IWidget parent = widget.getParent();
		if (!(parent instanceof IWidgetContainer)) {
			return;
		}
		((IWidgetContainer) parent).remove(widget);
	}

	public WidgetTreeList getWidgets() {
		return treeList;
	}

	public Optional<IWidget> getSelectedWidget() {
		return treeList.getSelectedWidget();
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
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawRect(0, 0, width, height, 0xF0F0F0F0);
		GL11.glPointSize(1);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
		for (float x = canvas.getX(); x < canvas.getX() + canvas.getWidth(); x += 2) {
			for (float y = canvas.getY(); y < canvas.getHeight(); y += 2) {
				bufferbuilder.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION);
				bufferbuilder.pos(x, y, 0).endVertex();
				tessellator.draw();
			}
		}
		window.draw(mouseX, mouseY);

		drawTooltips(mouseX, mouseY);
	}

	protected void drawTooltips(int mouseX, int mouseY) {
		GuiUtil.drawTooltips(this, buttonList, mouseX, mouseY);
		GlStateManager.pushMatrix();
		window.drawTooltip(mouseX, mouseY);
		GlStateManager.popMatrix();
	}

	@Override
	public void initGui() {
		super.initGui();
		window.init(0, 0);
		//if(!inited){
		//object.setLocation((canvas.getWidth() - object.getWidth()) / 2, (canvas.getHeight() - object.getHeight()) / 2);
		//overlay.setRegion(object.getRegion());
		//}
		//inited = true;
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		window.setSize(width, height);
		float windowLength = (width / 16.0F) * 3.0F;
		//canvas.setSize(width - Math.round(windowLength * 2.0F), height).setLocation(Math.round(windowLength), 0);
		canvas.setSize(width - 180, height);
		//inspector.setSize(Math.round(windowLength), height);
		//creator.setSize(Math.round(windowLength), height).setLocation(width - Math.round(windowLength), 0);
		inspector.setHeight(height);
		creator.setHeight(height / 2);
		tree.setHeight(height / 2 + 1);
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

		window.actionOnHovered(widget -> widget.hasListener(MouseEvent.MOUSE_DOWN), widget -> window.dispatchEvent(new MouseEvent(widget, MouseEvent.MOUSE_DOWN, mouseX, mouseY, mouseButton)), true);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		super.mouseReleased(mouseX, mouseY, mouseButton);

		window.actionOnHovered(widget -> widget.hasListener(MouseEvent.MOUSE_UP), widget -> window.dispatchEvent(new MouseEvent(widget, MouseEvent.MOUSE_UP, mouseX, mouseY, mouseButton)), true);
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
		return width;
	}

	@Override
	public int getGuiTop() {
		return width;
	}

	@Override
	public int getSizeX() {
		return width;
	}

	@Override
	public int getSizeY() {
		return width;
	}

	@Override
	public Minecraft getMC() {
		return mc;
	}
}
