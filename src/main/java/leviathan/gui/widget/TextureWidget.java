package leviathan.gui.widget;

import net.minecraft.client.renderer.GlStateManager;

import leviathan.api.gui.ITextureWidget;
import leviathan.api.properties.IPropertyCollection;
import leviathan.api.render.IDrawable;
import leviathan.gui.properties.JEGSerializers;
import leviathan.utils.Drawable;

public class TextureWidget extends Widget implements ITextureWidget {
	private IDrawable drawable;

	public TextureWidget(int xPos, int yPos, int width, int height, IDrawable drawable) {
		super(xPos, yPos, width, height);
		this.drawable = drawable;
	}

	@Override
	public void addProperties(IPropertyCollection collection) {
		super.addProperties(collection);
		collection.addProperties(TextureWidget.class, creator -> creator.add("texture", Drawable.MISSING, ITextureWidget::getDrawable, ITextureWidget::setDrawable, JEGSerializers.DRAWABLE));
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		GlStateManager.enableAlpha();
		drawable.draw(0, 0, width, height);
		GlStateManager.disableAlpha();
	}

	@Override
	public void setDrawable(IDrawable drawable) {
		this.drawable = drawable;
	}

	@Override
	public IDrawable getDrawable() {
		return drawable;
	}
}
