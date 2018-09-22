package leviathan.gui.widget;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.GlStateManager;

import leviathan.api.gui.ITextWidget;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.text.ITextStyle;
import leviathan.utils.GuiUtil;
import static leviathan.gui.widget.LabelWidget.FONT_RENDERER;

public class SplitTextWidget extends Widget implements ITextWidget {

	private List<String> lines = new ArrayList<>();
	private String rawText;
	private ITextStyle style;

	public SplitTextWidget(int xPos, int yPos, int width, String rawText, WidgetAlignment align, ITextStyle style) {
		super(xPos, yPos, width, 0);
		this.rawText = rawText;
		this.style = style;
		setAlign(align);
		boolean uni = FONT_RENDERER.getUnicodeFlag();
		FONT_RENDERER.setUnicodeFlag(style.isUnicode());
		this.lines.addAll(FONT_RENDERER.listFormattedStringToWidth(GuiUtil.getFormattedString(style, rawText), width));
		FONT_RENDERER.setUnicodeFlag(uni);
		setHeight(lines.size() * FONT_RENDERER.FONT_HEIGHT);
	}

	@Override
	public Collection<String> getLines() {
		return lines;
	}

	@Override
	public ITextWidget setText(String text) {
		this.rawText = text;
		boolean uni = FONT_RENDERER.getUnicodeFlag();
		FONT_RENDERER.setUnicodeFlag(style.isUnicode());
		lines.clear();
		lines.addAll(FONT_RENDERER.listFormattedStringToWidth(GuiUtil.getFormattedString(style, rawText), getWidth()));
		FONT_RENDERER.setUnicodeFlag(uni);
		setHeight(lines.size() * FONT_RENDERER.FONT_HEIGHT);
		return this;
	}

	@Override
	public Map<ITextStyle, String> getRawLines() {
		return ImmutableMap.of(style, rawText);
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		boolean unicode = FONT_RENDERER.getUnicodeFlag();
		FONT_RENDERER.setUnicodeFlag(style.isUnicode());
		int posY = 0;
		for (String text : lines) {
			int posX = getWidth() - FONT_RENDERER.getStringWidth(text);
			posX *= getAlign().getXOffset();
			FONT_RENDERER.drawString(text, posX, posY, style.getColor());
			posY += FONT_RENDERER.FONT_HEIGHT;
		}
		FONT_RENDERER.setUnicodeFlag(unicode);
		GlStateManager.color(1.0f, 1.0f, 1.0f);
	}
}
