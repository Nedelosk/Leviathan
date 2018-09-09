package leviathan.gui.workspace;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetType;

public class WidgetTreeEntry {
	protected final IWidgetType type;
	protected final WidgetTreeList treeList;
	protected final List<WidgetTreeEntry> children = new ArrayList<>();
	protected String name;
	@Nullable
	protected String parent;
	protected boolean valid;

	public WidgetTreeEntry(WidgetTreeList treeList, String name, IWidgetType type) {
		this.treeList = treeList;
		this.name = name;
		this.type = type;
	}

	public boolean addChild(WidgetTreeEntry entry) {
		if (children.add(entry)) {
			entry.validate();
			entry.setParent(name);
			return true;
		}
		return false;
	}

	public boolean removeChild(WidgetTreeEntry entry) {
		if (children.remove(entry)) {
			entry.invalidate();
			entry.setParent(null);
			return true;
		}
		return false;
	}

	public void invalidate() {
		valid = false;
	}

	public void validate() {
		valid = true;
	}

	public boolean isValid() {
		return valid;
	}

	public List<WidgetTreeEntry> getChildren() {
		return children;
	}

	public IWidgetType getType() {
		return type;
	}

	public void setParent(@Nullable String parent) {
		this.parent = parent;
	}

	@Nullable
	public String getParent() {
		return parent;
	}

	public Optional<IWidget> getWidget() {
		return treeList.getWindow().getWidget(name);
	}

	public WidgetTreeList getTreeList() {
		return treeList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isRoot() {
		return false;
	}
}
