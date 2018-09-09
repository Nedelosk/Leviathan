package leviathan.gui.widget;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import leviathan.api.gui.ILabelWidget;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.gui.style.ITextStyle;
import leviathan.api.properties.IPropertyCollection;
import leviathan.gui.properties.JEGSerializers;
import leviathan.utils.GuiUtil;

public class LabelWidget extends Widget implements ILabelWidget {
	/* Constants */
	public static final FontRenderer FONT_RENDERER = Minecraft.getMinecraft().fontRenderer;

	/* Attributes - State */
	protected ITextStyle style;
	protected String text;
	protected String rawText;
	protected boolean textLength = false;

	public LabelWidget(String text, WidgetAlignment align, ITextStyle style) {
		this(0, 0, -1, FONT_RENDERER.FONT_HEIGHT, text, align, style);
	}

	public LabelWidget(int xPos, int yPos, int width, int height, String text, WidgetAlignment align, ITextStyle style) {
		super(xPos, yPos, width, height);
		textLength = width < 0;
		this.style = style;
		this.rawText = text;
		this.text = GuiUtil.getFormattedString(style, text);
		setAlign(align);
		if (textLength) {
			boolean uni = FONT_RENDERER.getUnicodeFlag();
			FONT_RENDERER.setUnicodeFlag(style.isUnicode());
			setWidth(FONT_RENDERER.getStringWidth(this.text));
			FONT_RENDERER.setUnicodeFlag(uni);
		}
	}

	@Override
	public ILabelWidget setStyle(ITextStyle style) {
		this.style = style;
		this.text = GuiUtil.getFormattedString(style, rawText);
		if (textLength) {
			boolean uni = FONT_RENDERER.getUnicodeFlag();
			FONT_RENDERER.setUnicodeFlag(style.isUnicode());
			setWidth(FONT_RENDERER.getStringWidth(this.text));
			FONT_RENDERER.setUnicodeFlag(uni);
		}
		return this;
	}

	@Override
	public void addProperties(IPropertyCollection collection) {
		super.addProperties(collection);
		collection.addProperties(LabelWidget.class, creator -> creator.add("text", "Label", LabelWidget::getRawText, LabelWidget::setText, JEGSerializers.STRING));
	}

	@Override
	public ITextStyle getStyle() {
		return style;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public ILabelWidget setText(String text) {
		this.rawText = text;
		this.text = GuiUtil.getFormattedString(style, text);
		if (textLength) {
			boolean uni = FONT_RENDERER.getUnicodeFlag();
			FONT_RENDERER.setUnicodeFlag(style.isUnicode());
			setWidth(FONT_RENDERER.getStringWidth(this.text));
			FONT_RENDERER.setUnicodeFlag(uni);
		}
		return this;
	}

	@Override
	public String getRawText() {
		return rawText;
	}

	@Override
	public Collection<String> getLines() {
		return Collections.singletonList(text);
	}

	@Override
	public Map<ITextStyle, String> getRawLines() {
		return ImmutableMap.of(style, rawText);
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		boolean unicode = FONT_RENDERER.getUnicodeFlag();
		FONT_RENDERER.setUnicodeFlag(style.isUnicode());
		FONT_RENDERER.drawString(text, 0, 0, style.getColor());
		FONT_RENDERER.setUnicodeFlag(unicode);
	}
}
