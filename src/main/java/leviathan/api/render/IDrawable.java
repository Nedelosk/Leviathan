package leviathan.api.render;

import net.minecraft.util.ResourceLocation;

public interface IDrawable {

	void setMode(DrawMode mode);

	DrawMode getMode();

	void setSprite(ResourceLocation textureLocation, int u, int v, int width, int height);

	void setSprite(ResourceLocation textureLocation, int u, int v, int width, int height, int textureWidth, int textureHeight);

	void setSprite(ISprite sprite);

	ISprite getSprite();

	ISpriteRenderer getRenderer();

	void draw(int xOffset, int yOffset, int width, int height);
}
