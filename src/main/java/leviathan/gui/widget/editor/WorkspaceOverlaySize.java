package leviathan.gui.widget.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import leviathan.api.gui.WidgetAlignment;
import leviathan.gui.GuiWorkspace;
import leviathan.gui.widget.Widget;
import org.lwjgl.opengl.GL11;

public class WorkspaceOverlaySize extends Widget {
	private static final int QUAD_SIZE = 20;

	private final WidgetAlignment alignment;

	public WorkspaceOverlaySize(WidgetAlignment alignment) {
		super(0, 0, QUAD_SIZE / 4, QUAD_SIZE / 4);
		this.alignment = alignment;
		setAlign(alignment);
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		if (!isMouseOver()) {
			return;
		}
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		int texX;
		int texY;
		switch (alignment) {
			case TOP_LEFT:
				texX = 8;
				texY = 0;
				break;
			case TOP_RIGHT:
				texX = 16;
				texY = 0;
				break;
			case BOTTOM_RIGHT:
				texX = 24;
				texY = 0;
				break;
			case TOP_CENTER:
				texX = 8;
				texY = 8;
				break;
			case MIDDLE_RIGHT:
				texX = 16;
				texY = 8;
				break;
			case BOTTOM_CENTER:
				texX = 24;
				texY = 8;
				break;
			case MIDDLE_LEFT:
				texX = 0;
				texY = 8;
				break;
			default:
				texX = 0;
				texY = 0;
				break;
		}

		GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
		GL11.glLineWidth(2.0F);
		GL11.glEnable(GL11.GL_LINE_STIPPLE);
		GL11.glLineStipple(2, (short) 52428);
		GlStateManager.disableAlpha();
		GlStateManager.scale(0.25F, 0.25F, 0.25F);
		//GlStateManager.translate(getWidth() * alignment.getXOffset(), getHeight() * alignment.getYOffset(), 0.0F);

		float x = 0;
		float y = 0;
		buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		buffer.pos(x, y, 0.0F).endVertex();
		buffer.pos(x + QUAD_SIZE, y, 0.0F).endVertex();
		buffer.pos(x + QUAD_SIZE, y + QUAD_SIZE, 0.0F).endVertex();
		buffer.pos(x, y + QUAD_SIZE, 0.0F).endVertex();
		tessellator.draw();
		GL11.glDisable(GL11.GL_LINE_STIPPLE);

		GlStateManager.enableAlpha();
		TextureManager manager = Minecraft.getMinecraft().renderEngine;
		manager.bindTexture(GuiWorkspace.BACKGROUND_TEXTURE.textureLocation);

		float center = (QUAD_SIZE - 8.0F) / 2.0F;
		drawTexturedModalRect(x + center, y + center, 128 + texX, texY, 8, 8);
		GlStateManager.disableAlpha();
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}
}
