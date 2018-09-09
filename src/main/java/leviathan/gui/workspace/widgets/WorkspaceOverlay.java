package leviathan.gui.workspace.widgets;

import java.util.Optional;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import leviathan.api.Region;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.gui.events.ElementEvent;
import leviathan.api.gui.events.MouseEvent;
import leviathan.gui.widget.WidgetContainer;
import leviathan.gui.workspace.GuiWorkspace;
import org.lwjgl.opengl.GL11;

public class WorkspaceOverlay extends WidgetContainer {
	private short pattern = (short) 52428;
	private int timer;
	private final GuiWorkspace creator;

	public WorkspaceOverlay(GuiWorkspace creator) {
		super(0, 0, 32, 32);
		this.creator = creator;
		for (WidgetAlignment alignment : WidgetAlignment.values()) {
			if (alignment == WidgetAlignment.MIDDLE_CENTER) {
				continue;
			}
			IWidget widget = new WorkspaceOverlaySize(alignment);
			widget.addListener(MouseEvent.MOUSE_DRAG_MOVE, event -> {
				int diffX = Math.round(event.getDiffX());
				int diffY = Math.round(event.getDiffY());
				Region region = getRegion().translate(-creator.canvas.getAbsoluteX(), -creator.canvas.getAbsoluteY());
				int x = region.getX();
				int y = region.getY();
				int w = region.getWidth();
				int h = region.getHeight();
				switch (alignment) {
					case BOTTOM_RIGHT:
						w += diffX;
						h += diffY;
						break;
					case MIDDLE_RIGHT:
						w += diffX;
						break;
					case BOTTOM_CENTER:
						h += diffY;
						break;
					case TOP_LEFT:
						x += diffX;
						y += diffY;
						w -= diffX;
						h -= diffY;
						break;
					case TOP_CENTER:
						y += diffY;
						h -= diffY;
						break;
					case TOP_RIGHT:
						y += diffY;
						w += diffX;
						h -= diffY;
						break;
					case MIDDLE_LEFT:
						x += diffX;
						w -= diffX;
						break;
					case BOTTOM_LEFT:
						x += diffX;
						w -= diffX;
						h += diffY;
						break;
					default:
						break;
				}
				Optional<IWidget> optionalSelected = creator.getSelectedWidget();
				if (optionalSelected.isPresent()) {
					IWidget selectedWidget = optionalSelected.get();
					/*selectedWidget.setRegion(new Region(x, y, w, h));
					setRegion(selectedWidget.getRegion());*/
					creator.setSelectedRegion(selectedWidget, new Region(x, y, w, h));
				}
			});
			add(widget);
		}
		setVisible(false);
	}

	@Override
	public void onCreation(IWindowWidget window) {
		super.onCreation(window);
		window.addListener(ElementEvent.REGION_CHANGE, event -> {
			IWidget origin = event.getSource();
			Optional<IWidget> optionalSelected = creator.getSelectedWidget();
			if (!optionalSelected.isPresent() || origin != optionalSelected.get() || !isVisible()) {
				return;
			}
			setRegion(event.getNewRegion());
		});
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		super.drawElement(mouseX, mouseY);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
		GL11.glLineWidth(2.0F);
		GL11.glEnable(GL11.GL_LINE_STIPPLE);
		GL11.glLineStipple(2, pattern);
		GlStateManager.disableAlpha();
		timer++;
		if (timer % 20 == 0) {
			pattern = (short) (~pattern);
			timer = 0;
		}
		float x = 0;
		float y = 0;
		buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		buffer.pos(x, y, 0.0F).endVertex();
		buffer.pos(x + getWidth(), y, 0.0F).endVertex();
		buffer.pos(x + getWidth(), y + getHeight(), 0.0F).endVertex();
		buffer.pos(x, y + getHeight(), 0.0F).endVertex();
		tessellator.draw();
		GL11.glDisable(GL11.GL_LINE_STIPPLE);
	}
}
