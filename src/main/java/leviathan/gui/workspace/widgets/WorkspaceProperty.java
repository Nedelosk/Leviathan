package leviathan.gui.workspace.widgets;

import javax.annotation.Nullable;

import org.apache.commons.lang3.text.WordUtils;

import leviathan.api.gui.ILabelWidget;
import leviathan.api.gui.IWidgetGroup;
import leviathan.api.gui.events.MouseEvent;
import leviathan.api.gui.events.TextEditEvent;
import leviathan.api.properties.IProperty;
import leviathan.api.properties.serializer.IElementSerializer;
import leviathan.api.properties.serializer.IRangeSerializer;
import leviathan.api.render.ISprite;
import leviathan.gui.widget.ColoredWidget;
import leviathan.gui.widget.TextEditWidget;
import leviathan.gui.widget.layouts.WidgetGroup;
import leviathan.utils.ResourceUtil;
import leviathan.utils.Sprite;

public class WorkspaceProperty extends WidgetGroup {
	@Nullable
	private final ILabelWidget label;
	@Nullable
	private final TextEditWidget edit;
	private final IElementSerializer serializer;
	private final IProperty property;

	public WorkspaceProperty(IProperty property) {
		super(0, 0, 100, 12);
		this.property = property;
		this.serializer = property.getSerializer();
		/*if(serializer instanceof ISubtypeSerializer) {
			setHeight(12);
			this.edit = null;
			this.label = null;
		}else{*/
		setHeight(24);
		IWidgetGroup group = pane(5, 11, 75, 10);
		group.add(new ColoredWidget(-2, -2, 63, 13, 0xFF9b9b9b));
		group.add(new ColoredWidget(-1, -1, 61, 11, 0xFF5e5e5e));
		if (serializer instanceof IRangeSerializer) {
			group.add(new Button(62, -1, new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 134, 16, 6, 3), true));
			group.add(new Button(62, 5, new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 128, 16, 6, 3), false));
		}
		this.edit = group.add(new TextEditWidget(0, 0, 60, 10));
		this.edit.addListener(TextEditEvent.FINISH, event -> property.setText(event.getNewValue()));
		this.label = pane(50, 12).label(WordUtils.capitalize(property.getName()));
		//}
	}

	@Override
	public void updateClient() {
		super.updateClient();
		try {
			if (edit != null && !edit.isFocused()) {
				String value = property.getText();
				if (!edit.getValue().equals(value)) {
					edit.setValue(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IProperty getProperty() {
		return property;
	}

	private class Button extends WidgetGroup {
		private final ColoredWidget colored;

		public Button(int xPos, int yPos, ISprite sprite, boolean next) {
			super(xPos, yPos, 8, 5);
			colored = add(new ColoredWidget(0, 0, 8, 5, 0xFF9b9b9b));
			addListener(MouseEvent.MOUSE_ENTER, event -> {
				colored.setColor(0xFFbababa);
			});
			addListener(MouseEvent.MOUSE_LEAVE, event -> {
				colored.setColor(0xFF9b9b9b);
			});

			addListener(MouseEvent.MOUSE_DOWN, event -> {
				if (edit != null) {
					if (next) {
						property.increaseValue();
					} else {
						property.decreaseValue();
					}
				}
			});
			drawable(1, 1, sprite);
		}

		@Override
		public boolean canMouseOver() {
			return true;
		}
	}
}
