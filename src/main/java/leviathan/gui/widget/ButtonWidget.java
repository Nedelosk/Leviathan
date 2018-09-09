package leviathan.gui.widget;

import java.util.function.Consumer;

import net.minecraft.client.renderer.GlStateManager;

import leviathan.api.gui.events.MouseEvent;
import leviathan.utils.SoundUtil;
import leviathan.utils.Sprite;

public class ButtonWidget extends Widget {
	/* Attributes - Final */
	private final Consumer<ButtonWidget> onClicked;
	private final Sprite[] textures = new Sprite[3];

	public ButtonWidget(int xPos, int yPos, int width, int height, Sprite disabledTexture, Sprite enabledTexture, Consumer<ButtonWidget> onClicked) {
		this(xPos, yPos, width, height, disabledTexture, enabledTexture, enabledTexture, onClicked);
	}

	public ButtonWidget(int xPos, int yPos, int width, int height, Sprite disabledTexture, Sprite enabledTexture, Sprite mouseOverTexture, Consumer<ButtonWidget> onClicked) {
		super(xPos, yPos, width, height);
		this.onClicked = onClicked;
		textures[0] = disabledTexture;
		textures[1] = enabledTexture;
		textures[2] = mouseOverTexture;
		addListener(MouseEvent.MOUSE_DOWN, event -> {
			if (!isEnabled()) {
				return;
			}
			onPressed();
			SoundUtil.playButtonClick();
		});
	}

	public ButtonWidget(int xPos, int yPos, Sprite texture, Consumer<ButtonWidget> onClicked) {
		super(xPos, yPos, texture.width, texture.height);
		this.onClicked = onClicked;
		for (int i = 0; i < 3; i++) {
			textures[i] = new Sprite(texture.textureLocation, texture.u, texture.v + texture.height * i, texture.width, texture.height);
		}
		addListener(MouseEvent.MOUSE_DOWN, event -> {
			if (!isEnabled()) {
				return;
			}
			onPressed();
			SoundUtil.playButtonClick();
		});
	}

	/*public ButtonElement(int xPos, int yPos, StandardButtonTextureSets textureSets, Consumer<ButtonElement> onClicked) {
		super(xPos, yPos, textureSets.getWidth(), textureSets.getHeight());
		this.onClicked = onClicked;
		for (int i = 0; i < 3; i++) {
			textures[i] = new Drawable(textureSets.getTexture(), textureSets.getX(), textureSets.getY() + textureSets.getHeight() * i, textureSets.getWidth(), textureSets.getHeight());
		}
		addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
			if (!enabled) {
				return;
			}
			onPressed();
			SoundUtil.playButtonClick();
		});
	}*/

	@Override
	public void drawElement(int mouseX, int mouseY) {
		GlStateManager.enableAlpha();
		boolean mouseOver = isMouseOver();
		int hoverState = getHoverState(mouseOver);
		Sprite texture = textures[hoverState];
		texture.draw(0, 0);
		GlStateManager.disableAlpha();
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}

	protected int getHoverState(boolean mouseOver) {
		int i = 1;

		if (!isEnabled()) {
			i = 0;
		} else if (mouseOver) {
			i = 2;
		}

		return i;
	}

	public void onPressed() {
		onClicked.accept(this);
	}

}
