package leviathan.gui.workspace.widgets;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import leviathan.api.Region;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetContainer;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.properties.IProperty;
import leviathan.api.properties.IPropertyCollection;
import leviathan.api.render.DrawMode;
import leviathan.gui.widget.ColoredWidget;
import leviathan.gui.widget.ScrollBarWidget;
import leviathan.gui.widget.ScrollableWidget;
import leviathan.gui.widget.WidgetContainer;
import leviathan.gui.workspace.GuiWorkspace;
import leviathan.gui.workspace.ITreeListListener;
import leviathan.gui.workspace.WidgetTreeEntry;
import leviathan.gui.workspace.WidgetTreeList;
import leviathan.utils.Drawable;
import leviathan.utils.ResourceUtil;
import leviathan.utils.Sprite;

public class WorkspaceInspector extends WorkspaceControl implements ITreeListListener {

	public final IWidgetContainer properties;
	private final ScrollBarWidget scrollBar;
	private final ScrollableWidget scrollable;

	public WorkspaceInspector(GuiWorkspace creator) {
		super(creator);
		setName("inspector");
		IWidgetContainer propertyPane = pane(4, 15, getWidth(), getHeight());
		this.properties = propertyPane.vertical(0,6);
		scrollBar = add(new ScrollBarWidget(-4, -4, 3, 32, new Drawable(DrawMode.REPEAT, new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 0, 64, 3, 5)), false, new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 3, 64, 3, 5)));
		scrollBar.setAlign(WidgetAlignment.BOTTOM_RIGHT);
		scrollBar.setVisible(true);
		scrollable = propertyPane.add(new ScrollableWidget(0, 0, 400, 400));
		scrollable.setContent(this.properties);
	}

	@Override
	protected void onRegionChange(Region oldRegion, Region newRegion) {
		super.onRegionChange(oldRegion, newRegion);
		updateScrollWidgets();
	}

	private void updateScrollWidgets() {
		scrollBar.setHeight(getHeight() - 19);
		scrollable.setSize(getWidth() - 4, getHeight() - 15);
		int invisibleArea = scrollable.getInvisibleArea();
		if (!properties.getElements().isEmpty() && invisibleArea > 0) {
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
		String title = "Inspector";
		drawRect(1, 1, getWidth() - 1, fontRenderer.FONT_HEIGHT + 4, 0xFFb7b7b7);
		fontRenderer.drawString(title, (getWidth() - fontRenderer.getStringWidth(title)) / 2, 4, 0xFFFFFF);
		/*int top = 16;
		for(IWidget widget : creator.canvas.getObjects()){
			GlStateManager.pushMatrix();
			GlStateManager.translate(4, top, 0.0F);
			drawEntry(getWidth() - 8, 12);
			GlStateManager.popMatrix();
			top+=12;
		}*/
	}

	private void drawEntry(int width, int height) {
		drawRect(0, 0, width, height, isMouseOver() ? 0xFFa0a0a0 : 0xFF666666);
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String title = "background";
		fontRenderer.drawString(title, (width - fontRenderer.getStringWidth(title)) / 2, (height - fontRenderer.FONT_HEIGHT) / 2, 0xFFFFFF);
	}

	@Override
	public void onEntrySelection(WidgetTreeList treeList, WidgetTreeEntry entry, @Nullable WidgetTreeEntry oldEntry) {
		Optional<IWidget> optionalWidget = entry.getWidget();
		properties.clear();
		if (optionalWidget.isPresent()) {
			IWidget widget = optionalWidget.get();
			IPropertyCollection collection = treeList.getSelectedCollection();
			for (IProperty property : collection.getProperties(widget)) {
				/*if(property.hasChildren()){
					Collection<IProperty> children = property.getChildren();
					IWidgetGroup subtypeGroup = properties.pane(82, 24 * children.size() + 12);
					subtypeGroup.add(new ColoredWidget(-5, 0, 82, 24 * children.size() + 12, 0xFF606060));
					IWidgetGroup vertical = subtypeGroup.vertical(82);
					vertical.label(WordUtils.capitalize(property.getName()), WidgetAlignment.TOP_CENTER).setYPosition(2);
					for(IProperty child : children){
						vertical.add(new WorkspaceProperty(child));
					}
				}else{
					properties.add(new WorkspaceProperty(property));
				}*/
				properties.add(addProperty(property));
			}
			properties.validate();
		}
		updateScrollWidgets();
	}

	protected IWidget addProperty(IProperty property) {
		if (property.hasChildren()) {
			Collection<IProperty> children = property.getChildren();
			IWidgetContainer subtypeGroup = new WidgetContainer(0, 0, 82, 0);
			subtypeGroup.add(new ColoredWidget(-5, 0, 82, 24 * children.size() + 12, 0xFF606060));
			IWidgetContainer vertical = subtypeGroup.vertical(82);
			vertical.label(WordUtils.capitalize(property.getName()), WidgetAlignment.TOP_CENTER).setYPosition(2);
			for (IProperty child : children) {
				vertical.add(addProperty(child));
			}
			vertical.validate();
			subtypeGroup.setHeight(vertical.getHeight());
			return subtypeGroup;
		}
		return new WorkspaceProperty(property);
	}
}
