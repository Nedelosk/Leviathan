package leviathan.gui.widget.editor;

import javax.annotation.Nullable;
import java.util.Optional;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;

import leviathan.api.geometry.Point;
import leviathan.api.geometry.RectTransform;
import leviathan.api.geometry.Region;
import leviathan.api.geometry.Vector;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetContainer;
import leviathan.api.events.MouseEvent;
import leviathan.editor.WidgetTreeEntry;
import leviathan.editor.WidgetTreeList;
import leviathan.gui.GuiWorkspace;
import leviathan.gui.ITreeListListener;
import leviathan.gui.widget.WidgetContainer;
import org.lwjgl.opengl.GL11;

public class WorkspaceCanvas extends WidgetContainer implements ITreeListListener {
	private static final float MIN_SCALE = 0.5F;
	private static final float MAX_SCALE = 2.0F;

	private final IWidgetContainer container;
	private final IWidget overlay;
	private final GuiWorkspace workspace;
	private float zoom;
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	private Region currentRegion;
	@Nullable
	private WidgetTreeList treeList;

	public WorkspaceCanvas(GuiWorkspace workspace) {
		super(90, 0, 32, 32);
		setName("canvas");
		this.workspace = workspace;
		container = container(0, 0, 32, 32);
		container.setName("canvas_container");
		overlay = container.add(new WorkspaceOverlay(this));
		zoom = 1.0F;
		addListener(MouseEvent.WHEEL, event -> {
			int dWheel = event.getDWheel();
			if(dWheel > 0){
				zoom +=0.25F;
			}else if(dWheel < 0){
				zoom -=0.25F;
			}
			zoom = MathHelper.clamp(zoom, MIN_SCALE, MAX_SCALE);
			RectTransform containerTransform = container.getTransform();
			containerTransform.setScale(new Vector(1 / zoom, 1 / zoom));
		});
		addListener(MouseEvent.MOUSE_DRAG_START, event -> {

		});
		addListener(MouseEvent.MOUSE_DRAG_MOVE, event -> {

		});
		addListener(MouseEvent.MOUSE_DRAG_END, event -> {

		});
	}

	@Override
	protected void onRegionChange(Region oldRegion, Region newRegion) {
		super.onRegionChange(oldRegion, newRegion);
		container.setSize(newRegion.getSize());
	}

	public IWidgetContainer getContainer() {
		return container;
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		GlStateManager.scale(1.0F / zoom, 1.0F / zoom, 1.0F);
		drawRect(0, 0, getWidth(), getHeight(), 0xF0F0F0F0);
		GL11.glPointSize(2);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
		for (float x = 0; x < getWidth(); x += 4) {
			for (float y = 0; y < getHeight(); y += 4) {
				bufferbuilder.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION);
				bufferbuilder.pos(x, y, 0).endVertex();
				tessellator.draw();
			}
		}
		super.drawElement(mouseX, mouseY);
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
			parent = container;
		}
		Point position = parent.getAbsolutePosition().subtract(container.getAbsolutePosition());
		overlay.setRegion(widget.getRegion().translate(position));
	}

	@Override
	public void onEntryCreation(WidgetTreeList treeList, WidgetTreeEntry entry) {
		IWidget widget = entry.getType().createWidget();
		widget.setName(entry.getName());
		container.add(widget);
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
		IWidgetContainer parent = widget.getParent();
		if (parent == null) {
			return;
		}
		parent.remove(widget);
	}

	public void setSelectedRegion(IWidget selectedWidget, Region newRegion) {
		Region containerRegion = container.getRegion();
		Region oldRegion = selectedWidget.getRegion();
		int xPos = newRegion.getX();
		int yPos = newRegion.getY();
		int width = newRegion.getWidth();
		int height = newRegion.getHeight();
		int distRight = containerRegion.getWidth() - (xPos + width);
		int distBottom = containerRegion.getHeight() - (yPos + height);
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
			xPos = containerRegion.getWidth() - width;
		}
		if (distBottom < 0) {
			if (height != oldRegion.getHeight()) {
				height = oldRegion.getHeight();
			}
			yPos = containerRegion.getHeight() - height;
		}
		selectedWidget.setRegion(new Region(xPos, yPos, width, height));
		IWidget parent = selectedWidget.getParent();
		if (parent == null) {
			parent = this;
		}
		Point position = parent.getAbsolutePosition().subtract(container.getAbsolutePosition());
		overlay.setRegion(selectedWidget.getRegion().translate(position));
	}

	public WidgetTreeList getTreeList() {
		if(treeList == null){
			treeList = workspace.treeList;
		}
		return treeList;
	}

	public Optional<IWidget> getSelectedWidget() {
		return getTreeList().getSelectedWidget();
	}
}
