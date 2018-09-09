package leviathan.utils;

import com.google.common.base.MoreObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.render.ISprite;

@SideOnly(Side.CLIENT)
public class Sprite implements ISprite {
	//Position on the Texture
	public final int u;
	public final int v;
	//Rectangle Size
	public final int width;
	public final int height;
	//Texture
	public final ResourceLocation textureLocation;
	//Texture Size
	private final int textureWidth;
	private final int textureHeight;

	public Sprite(ResourceLocation textureLocation, int u, int v, int width, int height) {
		this(textureLocation, u, v, width, height, 256, 256);
	}

	public Sprite(ResourceLocation textureLocation, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
		this.textureLocation = textureLocation;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	@Override
	public int getU() {
		return u;
	}

	@Override
	public int getV() {
		return v;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getTextureWidth() {
		return textureWidth;
	}

	@Override
	public int getTextureHeight() {
		return textureHeight;
	}

	@Override
	public ResourceLocation getLocation() {
		return textureLocation;
	}

	@Override
	public void draw(int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		textureManager.bindTexture(textureLocation);

		// Enable correct lighting.
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		int x = xOffset + maskLeft;
		int y = yOffset + maskTop;
		int u = this.u + maskLeft;
		int v = this.v + maskTop;
		int width = this.width - maskRight - maskLeft;
		int height = this.height - maskBottom - maskTop;

		Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("location", textureLocation)
			.add("u", u)
			.add("v", v)
			.add("w", width)
			.add("h", height)
			.add("texW", textureWidth)
			.add("texH", textureHeight)
			.toString();
	}
}
