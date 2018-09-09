package leviathan.gui.workspace.widgets;

import leviathan.api.gui.ILabelWidget;
import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetGroup;
import leviathan.api.gui.events.MouseEvent;
import leviathan.gui.widget.ColoredWidget;
import leviathan.gui.widget.layouts.WidgetGroup;
import leviathan.gui.workspace.WidgetTreeEntry;
import leviathan.gui.workspace.WidgetTreeList;

public class WorkspaceTreeElement extends WidgetGroup {
	public static final int DEFAULT_WIDTH = 75;
	public static final int DEFAULT_HEIGHT = 12;
	public static final int ELEMENT_DISTANCE = 3;

	private final WidgetTreeEntry entry;
	public boolean dragged = false;
	public int dragX;
	public int dragY;
	public final IWidgetGroup element;
	public final ColoredWidget color;
	public final ILabelWidget nameLabel;

	public WorkspaceTreeElement(WorkspaceTree tree, WidgetTreeEntry entry) {
		super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.entry = entry;
		element = pane(0, 0);
		color = element.add(new ColoredWidget(0, 0, width, height, 0xFF939393));
		nameLabel = element.label(entry.getName());
		nameLabel.setLocation(3, 2);
		for (WidgetTreeEntry child : entry.getChildren()) {
			add(new WorkspaceTreeElement(tree, child));
		}
		addListener(MouseEvent.MOUSE_DOWN, event -> {
			WidgetTreeList treeList = entry.getTreeList();
			treeList.selectEntry(entry.getName());
		});
		addListener(MouseEvent.MOUSE_DRAG_START, event -> {
			dragX = 0;
			dragY = 0;
			IWidgetGroup group = new WidgetGroup(0, 0, width, height);
			group.add(new ColoredWidget(0, 0, width, height, 0xFFaaaaaa));
			group.label(entry.getName()).setLocation(3, 2);
			element.setVisible(false);
			tree.setDragElement(this, group);
		});
		addListener(MouseEvent.MOUSE_DRAG_MOVE, event -> {
			dragX += event.getDiffX();
			dragY += event.getDiffY();
			tree.updateDragPosition(dragX, dragY);
		});
		addListener(MouseEvent.MOUSE_DRAG_END, event -> {
			dragX = 0;
			dragY = 0;
			element.setVisible(true);
			tree.setDragElement(this, null);
			IWidget hoverElement = tree.treeElements.calculateHoverElement(widget -> widget instanceof WorkspaceTreeElement);
			if (hoverElement instanceof WorkspaceTreeElement && hoverElement != this) {
				WorkspaceTreeElement hover = (WorkspaceTreeElement) hoverElement;
				WidgetTreeList treeList = entry.getTreeList();
				treeList.moveEntry(entry.getName(), hover.entry.getName());
			}
		});
	}
}
