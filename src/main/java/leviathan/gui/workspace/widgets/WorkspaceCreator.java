package leviathan.gui.workspace.widgets;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import net.minecraftforge.fml.common.registry.GameRegistry;

import leviathan.api.Region;
import leviathan.api.WidgetTab;
import leviathan.api.gui.GuiConstants;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetGroup;
import leviathan.api.gui.IWidgetType;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.gui.events.MouseEvent;
import leviathan.api.render.DrawMode;
import leviathan.gui.widget.ColoredWidget;
import leviathan.gui.widget.ScrollBarWidget;
import leviathan.gui.widget.ScrollableWidget;
import leviathan.gui.widget.layouts.VerticalLayout;
import leviathan.gui.widget.layouts.WidgetGroup;
import leviathan.gui.workspace.GuiWorkspace;
import leviathan.utils.Drawable;
import leviathan.utils.ResourceUtil;
import leviathan.utils.Sprite;

public class WorkspaceCreator extends WorkspaceControl {
	private final Tabs tabs;

	private final Buttons buttons;

	private final ScrollBarWidget scrollBar;
	private final ScrollableWidget scrollable;

	public WorkspaceCreator(GuiWorkspace creator) {
		super(creator);
		IWidgetGroup group = pane(8, 27, width - 1, height - 15);
		this.buttons = group.add(new Buttons());
		scrollBar = add(new ScrollBarWidget(-4, -4, 3, 32, new Drawable(DrawMode.REPEAT, new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 0, 64, 3, 5)), false, new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 3, 64, 3, 5)));
		scrollBar.setAlign(WidgetAlignment.BOTTOM_RIGHT);
		scrollBar.setVisible(true);
		scrollable = group.add(new ScrollableWidget(0, 0, 400, 400));
		scrollable.setContent(this.buttons);
		tabs = add(new Tabs(1, 13));
		tabs.select(tabs.selected);
	}

	@Override
	protected void onRegionChange(Region oldRegion, Region newRegion) {
		super.onRegionChange(oldRegion, newRegion);
		updateScroll();
	}

	private void updateScroll() {
		buttons.updateButtons();
		scrollBar.setHeight(height - 31);
		scrollable.setSize(width - 4, height - 29);
		int invisibleArea = scrollable.getInvisibleArea();
		if (invisibleArea > 0) {
			if (scrollBar.getBackground() != null) {
				scrollBar.getBackground().setHeight(scrollBar.getHeight());
			}
			scrollBar.setParameters(scrollable, 0, invisibleArea, 1);
			scrollBar.setVisible(true);
		} else {
			scrollBar.setVisible(false);
		}
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		super.drawElement(mouseX, mouseY);
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String title = "Creator";
		drawRect(1, 1, getWidth() - 1, fontRenderer.FONT_HEIGHT + 4, 0xFFb7b7b7);
		fontRenderer.drawString(title, (getWidth() - fontRenderer.getStringWidth(title)) / 2, 4, 0xFFFFFF);
	}

	public void addObject(WorkspaceObject object) {
	}

	public boolean hasSelectedType() {
		return buttons.selected != null;
	}

	@Nullable
	public IWidgetType getSelectedType() {
		return buttons.selected == null ? null : buttons.selected.type;
	}

	public void deselectType() {
		if (buttons.selected != null) {
			buttons.selected.selectionBackground.setVisible(false);
			buttons.selected = null;
		}
	}

	private class Tabs extends WidgetGroup {
		private Tab selected;

		public Tabs(int xPos, int yPos) {
			super(xPos, yPos, 90, 12);
			selected = add(new Tab(0, 0, WidgetTab.OBJECTS));
			add(new Tab(45, 0, WidgetTab.GROUPS));
		}

		public void select(Tab tab) {
			selected.colored.setColor(0xFF939393);
			tab.colored.setColor(0xFFaaaaaa);
			this.selected = tab;
			updateScroll();
		}

		@Override
		public boolean canMouseOver() {
			return true;
		}
	}

	private class Tab extends WidgetGroup {
		private final WidgetTab tab;
		private final ColoredWidget colored;

		private final List<IWidgetType> types;

		public Tab(int xPos, int yPos, WidgetTab tab) {
			super(xPos, yPos, 45, 12);
			this.tab = tab;
			this.types = GameRegistry.findRegistry(IWidgetType.class)
				.getValuesCollection()
				.stream()
				.filter(type -> type.getTab() == tab)
				.collect(Collectors.toList());
			addTooltip(tab.name());
			colored = add(new ColoredWidget(0, 0, width, height, 0xFF939393));
			label(tab.name(), -1, 8, WidgetAlignment.MIDDLE_CENTER, GuiConstants.DEFAULT_STYLE);
			addListener(MouseEvent.MOUSE_DOWN, event -> tabs.select(this));
		}

		@Override
		public boolean canMouseOver() {
			return true;
		}
	}

	private class Buttons extends VerticalLayout {
		@Nullable
		private Button selected;

		public Buttons() {
			super(0, 0, 90 - 12);
			setDistance(1);
		}

		public void updateButtons() {
			clear();
			List<IWidgetType> types = tabs.selected.types;
			for (int y = 0; y < types.size() / 4 + 1; y++) {
				IWidgetGroup group = horizontal(18).setDistance(1);
				for (int x = 0; x < 4; x++) {
					int index = y * 4 + x;
					if (index < types.size()) {
						group.add(new Button(types.get(index)));
					}
				}
			}
		}
	}

	private class Button extends WidgetGroup {
		private final IWidgetType type;
		private final IWidget selectionBackground;

		public Button(IWidgetType type) {
			super(0, 0, 18, 18);
			this.type = type;
			addTooltip(type.getRegistryName().getResourcePath());
			selectionBackground = add(new ColoredWidget(0, 0, 18, 18, 0xFF3e84ad));
			selectionBackground.setVisible(false);
			add(new ColoredWidget(1, 1, 16, 16, 0xFF939393));
			add(new ColoredWidget(2, 2, 14, 14, 0xFFaaaaaa));
			addListener(MouseEvent.MOUSE_DOWN, event -> {
				if (buttons.selected != null) {
					buttons.selected.selectionBackground.setVisible(false);
				}
				buttons.selected = this;
				selectionBackground.setVisible(true);
			});
		}

		@Override
		public boolean canMouseOver() {
			return true;
		}
	}
}
