package leviathan.gui.workspace.widgets;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import leviathan.api.Region;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetGroup;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.render.DrawMode;
import leviathan.gui.widget.ScrollBarWidget;
import leviathan.gui.widget.ScrollableWidget;
import leviathan.gui.workspace.GuiWorkspace;
import leviathan.gui.workspace.ITreeListListener;
import leviathan.gui.workspace.WidgetTreeEntry;
import leviathan.gui.workspace.WidgetTreeList;
import leviathan.utils.Drawable;
import leviathan.utils.ResourceUtil;
import leviathan.utils.Sprite;

public class WorkspaceTree extends WorkspaceControl implements ITreeListListener {
	private final ScrollBarWidget scrollBar;
	private final ScrollableWidget scrollable;
	public final IWidgetGroup treeElements;
	private IWidgetGroup dragContainer;

	private final Map<String, WorkspaceTreeElement> elementByName = new HashMap<>();

	public WorkspaceTree(GuiWorkspace creator) {
		super(creator);
		IWidgetGroup group = pane(2, 15, width - 8, height - 19);
		dragContainer = group.pane(group.getWidth(), group.getHeight());
		treeElements = group.vertical(group.getWidth()).setDistance(3);
		scrollBar = add(new ScrollBarWidget(-4, -4, 3, 32, new Drawable(DrawMode.REPEAT, new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 0, 64, 3, 5)), false, new Sprite(ResourceUtil.guiLocation("backgrounds.png"), 3, 64, 3, 5)));
		scrollBar.setAlign(WidgetAlignment.BOTTOM_RIGHT);
		scrollable = group.add(new ScrollableWidget(0, 0, 400, 400));
		scrollable.setContent(treeElements);
	}

	@Override
	protected void onRegionChange(Region oldRegion, Region newRegion) {
		super.onRegionChange(oldRegion, newRegion);
		updateScroll();
	}

	private void updateScroll() {
		scrollBar.setHeight(height - 19);
		scrollable.setSize(width - 4, height - 19);
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

	public void setDragElement(WorkspaceTreeElement treeElement, @Nullable IWidget element) {
		dragContainer.clear();
		if (element != null) {
			element.setLocation(treeElement.getAbsoluteX() - dragContainer.getAbsoluteX(), treeElement.getAbsoluteY() - dragContainer.getAbsoluteY());
			dragContainer.add(element);
		}
	}

	public void updateDragPosition(int x, int y) {
		IWidget widget = dragContainer.getLastElement();
		if (widget == null) {
			return;
		}
		widget.setOffset(x, y);
	}

	@Override
	public void onEntryCreation(WidgetTreeList treeList, WidgetTreeEntry entry) {
		elementByName.put(entry.getName(), treeElements.add(new WorkspaceTreeElement(this, entry)));
		updateScroll();
	}

	@Override
	public void onEntryRename(WidgetTreeList treeList, WidgetTreeEntry entry, String oldName, String newName) {
		WorkspaceTreeElement treeElement = elementByName.get(oldName);
		if (treeElement == null) {
			return;
		}
		treeElement.nameLabel.setText(newName);
		elementByName.remove(oldName);
		elementByName.put(newName, treeElement);
	}

	@Override
	public void onEntryDeletion(WidgetTreeList treeList, WidgetTreeEntry entry) {
		WorkspaceTreeElement element = elementByName.get(entry.getName());
		if (element == null) {
			return;
		}
		String parentName = entry.getParent();
		if (parentName == null) {
			return;
		}
		WidgetTreeEntry parentEntry = treeList.getEntry(parentName);
		if (parentEntry == null) {
			return;
		}
		IWidgetGroup parent = parentEntry.isRoot() ? treeElements : elementByName.get(parentName);
		if (parent == null) {
			return;
		}
		parent.remove(element);
		elementByName.remove(entry.getName());
	}

	@Override
	public void onEntryTransfer(WidgetTreeList treeList, WidgetTreeEntry entry, WidgetTreeEntry oldParent, WidgetTreeEntry newParent) {
		WorkspaceTreeElement element = elementByName.get(entry.getName());
		WorkspaceTreeElement oldElement = elementByName.get(oldParent.getName());
		WorkspaceTreeElement newElement = elementByName.get(newParent.getName());
		if (element == null || oldElement == null && !oldParent.isRoot() || newElement == null && !oldParent.isRoot()) {
			return;
		}
		oldElement.remove(element);
		newElement.add(element);
	}

	@Override
	public void onEntrySelection(WidgetTreeList treeList, WidgetTreeEntry entry, @Nullable WidgetTreeEntry oldEntry) {
		if (oldEntry != null) {
			WorkspaceTreeElement oldElement = elementByName.get(oldEntry.getName());
			if (oldElement != null) {
				oldElement.color.setColor(0xFF939393);
			}
		}
		WorkspaceTreeElement element = elementByName.get(entry.getName());
		if (element != null) {
			element.color.setColor(0xFFaaaaaa);
		}
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		super.drawElement(mouseX, mouseY);
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String title = "Widget Tree";
		drawRect(1, 1, getWidth() - 1, fontRenderer.FONT_HEIGHT + 4, 0xFFb7b7b7);
		fontRenderer.drawString(title, (getWidth() - fontRenderer.getStringWidth(title)) / 2, 4, 0xFFFFFF);
	}
}
