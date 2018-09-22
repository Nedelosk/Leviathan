package leviathan.widgets;

import leviathan.api.geometry.HorizontalAlignment;
import leviathan.api.geometry.ITransformProvider;
import leviathan.api.geometry.Vector;
import leviathan.api.geometry.VerticalAlignment;
import leviathan.api.text.ITextStyle;
import leviathan.api.widgets.ILabelWidget;
import leviathan.utils.GuiUtil;

public class LabelWidget extends Widget implements ILabelWidget {

	private String text;
	private String formattedText;
	private ITextStyle textStyle;
	private HorizontalAlignment horizontalAlignment;
	private VerticalAlignment verticalAlignment;
	private boolean autoSize;

	public static LabelWidget create(String name, float x, float y, float width, float height, String text, ITextStyle textStyle, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, boolean autoSize){
		return autoSize ? create(name, x, y, text, textStyle, horizontalAlignment, verticalAlignment) : create(name, x, y, width, height, text, textStyle, horizontalAlignment, verticalAlignment);
	}

	public static LabelWidget create(String name, float x, float y, String text, ITextStyle textStyle, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment){
		int width = GuiUtil.getTextWidth(text, textStyle);
		int height = GuiUtil.getTextHeight(text, textStyle);
		return new LabelWidget(name, x, y, width, height, text, textStyle, horizontalAlignment, verticalAlignment, true);
	}

	public static LabelWidget create(String name, float x, float y, float width, float height,  String text, ITextStyle textStyle, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment){
		return new LabelWidget(name, x, y, width, height, text, textStyle, horizontalAlignment, verticalAlignment, false);
	}

	private LabelWidget(String name, float x, float y, float width, float height, String text, ITextStyle textStyle, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, boolean autoSize) {
		super(name, x, y, width, height);
		this.text = text;
		this.textStyle = textStyle;
		this.formattedText = GuiUtil.getFormattedString(textStyle, text);
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
		this.autoSize = autoSize;
		setAlignment(horizontalAlignment, verticalAlignment);
	}

	@Override
	protected void drawWidget() {
		super.drawWidget();
		GuiUtil.drawText(formattedText, 0, 0, textStyle);
	}

	public String getFormattedText() {
		return formattedText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.formattedText = GuiUtil.getFormattedString(textStyle, text);
	}

	public ITextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(ITextStyle textStyle) {
		this.textStyle = textStyle;
		this.formattedText = GuiUtil.getFormattedString(textStyle, text);
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
		setAlignment(horizontalAlignment, verticalAlignment);
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
		setAlignment(horizontalAlignment, verticalAlignment);
	}

	@Override
	public ITransformProvider setAlignment(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
		return super.setAlignment(horizontalAlignment, verticalAlignment);
	}

	public boolean isAutoSize() {
		return autoSize;
	}

	public void setAutoSize(boolean autoSize) {
		if(autoSize && !this.autoSize){
			Vector size = new Vector(GuiUtil.getTextWidth(text, textStyle), GuiUtil.getTextHeight(text, textStyle));
			setSize(size);
		}
		this.autoSize = autoSize;
	}
}
