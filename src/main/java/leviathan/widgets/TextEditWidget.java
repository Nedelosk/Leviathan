package leviathan.widgets;

import net.minecraft.client.gui.GuiTextField;

import leviathan.utils.GuiUtil;

public class TextEditWidget extends Widget {
	private final GuiTextField field;

	public TextEditWidget(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
		field = new GuiTextField(0, GuiUtil.getFontRenderer(), x, y, width, height);
	}
}
