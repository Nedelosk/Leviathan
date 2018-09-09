package leviathan.gui.workspace.widgets;

import java.util.ArrayList;
import java.util.Collection;

import leviathan.api.gui.IWidget;
import leviathan.gui.widget.WidgetContainer;

public class WorkspaceCanvas extends WidgetContainer {
	private Collection<IWidget> objects = new ArrayList<>();

	public WorkspaceCanvas() {
		super(90, 0, 32, 32);
		setName("canvas");
	}

	public void addObject(WorkspaceObject object) {
		add(object);
		objects.add(object);
	}

	public Collection<IWidget> getObjects() {
		return objects;
	}
}
