package leviathan.gui.widget;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

import leviathan.api.Region;
import leviathan.api.gui.IScrollable;
import leviathan.api.gui.ITextureWidget;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.render.IDrawable;
import leviathan.gui.widget.layouts.WidgetGroup;
import leviathan.utils.Sprite;
import org.lwjgl.input.Mouse;

public class ScrollBarWidget extends WidgetGroup {
	/* Attributes - Final */
	private final ITextureWidget slider;
	@Nullable
	private final ITextureWidget background;
	private final boolean hasBorder;

	/* Attributes - State */
	private boolean isScrolling;
	private boolean wasClicked;
	private boolean vertical = false;
	private int currentValue;
	private final WidgetGroup interactionField;
	private int initialMouseClick;

	/* Attributes - Parameters */
	@Nullable
	private IScrollable listener;
	private int minValue;
	private int maxValue;
	private int step;

	public ScrollBarWidget(int xPos, int yPos, int width, int height, Sprite sliderTexture) {
		super(xPos, yPos, width, height);

		interactionField = add(new WidgetGroup(0, 0, width, height));
		isScrolling = false;
		hasBorder = false;
		wasClicked = false;
		visible = true;
		slider = interactionField.drawable(sliderTexture);
		background = null;
	}

	public ScrollBarWidget(int xPos, int yPos, Sprite backgroundTexture, boolean hasBorder, Sprite sliderTexture) {
		super(xPos, yPos, backgroundTexture.width, backgroundTexture.height);

		int offset = hasBorder ? 1 : 0;

		interactionField = new WidgetGroup(offset, offset, hasBorder ? width - 2 : width, hasBorder ? height - 2 : height);
		isScrolling = false;
		this.hasBorder = hasBorder;
		wasClicked = false;
		visible = true;

		background = drawable(backgroundTexture);
		slider = interactionField.drawable(sliderTexture);
		add(interactionField);
	}

	public ScrollBarWidget(int xPos, int yPos, int width, int height, IDrawable backgroundTexture, boolean hasBorder, Sprite sliderTexture) {
		super(xPos, yPos, width, height);

		int offset = hasBorder ? 1 : 0;

		interactionField = new WidgetGroup(offset, offset, hasBorder ? width - 2 : width, hasBorder ? height - 2 : height);
		isScrolling = false;
		wasClicked = false;
		visible = true;
		this.hasBorder = hasBorder;

		background = drawable(backgroundTexture);
		slider = interactionField.drawable(sliderTexture);
		add(interactionField);
	}

	public ScrollBarWidget setVertical() {
		this.vertical = true;
		return this;
	}

	public ScrollBarWidget setParameters(IScrollable listener, int minValue, int maxValue, int step) {
		this.listener = listener;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;

		setValue(currentValue);
		return this;
	}

	@Override
	public void onRegionChange(Region oldRegion, Region newRegion) {
		super.onRegionChange(oldRegion, newRegion);
		interactionField.setSize(hasBorder ? newRegion.getWidth() - 2 : newRegion.getWidth(), hasBorder ? newRegion.getHeight() - 2 : newRegion.getHeight());
	}

	@Nullable
	public ITextureWidget getBackground() {
		return background;
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}

	public int getValue() {
		return MathHelper.clamp(currentValue, minValue, maxValue);
	}

	public ScrollBarWidget setValue(int value) {
		currentValue = MathHelper.clamp(value, minValue, maxValue);
		if (listener != null) {
			listener.onScroll(currentValue);
		}
		if (vertical) {
			int offset;
			if (value >= maxValue) {
				offset = interactionField.getWidth() - slider.getWidth();
			} else if (value <= minValue) {
				offset = 0;
			} else {
				offset = (int) (((float) (currentValue - minValue) / (maxValue - minValue)) * (float) (interactionField.getWidth() - slider.getWidth()));
			}
			slider.setXPosition(offset);
		} else {
			int offset;
			if (value >= maxValue) {
				offset = interactionField.getHeight() - slider.getHeight();
			} else if (value <= minValue) {
				offset = 0;
			} else {
				offset = (int) (((float) (currentValue - minValue) / (maxValue - minValue)) * (float) (interactionField.getHeight() - slider.getHeight()));
			}
			slider.setYPosition(offset);
		}
		return this;
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		if (!isVisible()) {
			return;
		}
		IWindowWidget window = getWindow();
		updateSlider(window.getRelativeMouseX(interactionField), window.getRelativeMouseY(interactionField));

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		super.drawElement(mouseX, mouseY);
	}

	private void updateSlider(int mouseX, int mouseY) {
		boolean mouseDown = Mouse.isButtonDown(0);

		if (listener == null || listener.isFocused(mouseX, mouseY)) {
			int wheel = Mouse.getDWheel();
			if (wheel > 0) {
				setValue(currentValue - step);
				return;
			} else if (wheel < 0) {
				setValue(currentValue + step);
				return;
			}
		}

		//The position of the mouse relative to the position of the widget
		int pos = vertical ? mouseX - interactionField.getX() : mouseY - interactionField.getY();

		if (!mouseDown && wasClicked) {
			wasClicked = false;
		}

		//Not clicked and scrolling -> stop scrolling
		if (!mouseDown && isScrolling) {
			this.isScrolling = false;
		}

		//Clicked on the slider and scrolling
		if (this.isScrolling) {
			int range = maxValue - minValue;
			float value = (float) (pos - initialMouseClick) / (float) (vertical ? (interactionField.getWidth() - slider.getWidth()) : (interactionField.getHeight() - slider.getHeight()));
			value *= (float) range;
			if (value < (float) step / 2f) {
				setValue(minValue);
			} else if (value > maxValue - ((float) step / 2f)) {
				setValue(maxValue);
			} else {
				setValue((int) (minValue + (float) step * Math.round(value)));
			}
		} else if (slider.isMouseOver()) { //clicked on the slider
			if (mouseDown) {
				isScrolling = true;
				initialMouseClick = vertical ? pos - slider.getX() : pos - slider.getY();
			}
		} else if (mouseDown && !wasClicked && isMouseOver()) { //clicked on the bar but not on the slider
			int range = maxValue - minValue;
			float value = ((float) pos - (vertical ? slider.getWidth() : slider.getHeight()) / 2.0F) / (float) (vertical ? (interactionField.getWidth() - slider.getWidth()) : (interactionField.getHeight() - slider.getHeight()));
			value *= (float) range;
			if (value < (float) step / 2f) {
				setValue(minValue);
			} else if (value > maxValue - ((float) step / 2f)) {
				setValue(maxValue);
			} else {
				setValue((int) (minValue + (float) step * Math.round(value)));
			}
			wasClicked = true;
		}
	}
}
