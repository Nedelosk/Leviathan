package leviathan.api.render;

public interface ISpriteRenderer {

	void draw(int xOffset, int yOffset, int width, int height);

	int getWidth();

	int getHeight();
}
