package leviathan.gui.widget;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

import leviathan.api.gui.IValueWidget;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.gui.events.FocusEvent;
import leviathan.api.gui.events.KeyEvent;
import leviathan.api.gui.events.MouseEvent;
import leviathan.api.gui.events.TextEditEvent;

public class TextEditWidget extends Widget implements IValueWidget<String> {

	private final GuiTextField field;
	@Nullable
	private String old;

	public TextEditWidget(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
		field = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, width, height);
		field.setEnableBackgroundDrawing(false);
		addListener(KeyEvent.KEY_DOWN, event -> {
			String oldText = field.getText();
			this.field.textboxKeyTyped(event.getCharacter(), event.getKey());
			final String text = field.getText();
			if (!text.equals(oldText)) {
				dispatchEvent(new TextEditEvent(this, text, oldText));
			}
		});
		addListener(MouseEvent.MOUSE_DOWN, event -> {
			IWindowWidget windowElement = getWindow();
			this.field.mouseClicked(windowElement.getRelativeMouseX(this), windowElement.getRelativeMouseY(this), event.getButton());
		});
		addListener(FocusEvent.GAIN, event -> {
			this.field.setFocused(true);
			this.old = field.getText();
		});
		addListener(FocusEvent.LOSE, event -> {
			this.field.setFocused(false);
			dispatchEvent(new TextEditEvent.Finish(this, field.getText(), old));
		});
	}

	public TextEditWidget setMaxLength(int maxLength) {
		field.setMaxStringLength(maxLength);
		return this;
	}

	public TextEditWidget setValidator(Predicate<String> validator) {
		field.setValidator(validator::test);
		return this;
	}

	public GuiTextField getField() {
		return field;
	}

	@Override
	public String getValue() {
		return field.getText();
	}

	@Override
	public void setValue(String value) {
		if (!field.getText().equals(value)) {
			field.setText(value);
		}
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		field.drawTextBox();
	}

	@Override
	public boolean canFocus() {
		return true;
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}
}
