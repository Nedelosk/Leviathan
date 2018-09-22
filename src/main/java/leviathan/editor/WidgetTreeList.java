package leviathan.editor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetType;
import leviathan.api.gui.IWindowWidget;
import leviathan.api.gui.WidgetType;
import leviathan.api.properties.IPropertyCollection;
import leviathan.gui.ITreeListListener;

public class WidgetTreeList {
	public static final String DEFAULT_NAME = "Element %s";

	private final Map<String, WidgetTreeEntry> entries = new HashMap<>();
	private final RootEntry root;
	private final IWindowWidget window;
	private final Set<ITreeListListener> listeners = new HashSet<>();
	private String selectedEntry;
	private IPropertyCollection selectedCollection;
	@Nullable
	private String dragEntry;

	public WidgetTreeList(IWidget rootWidget) {
		this.root = new RootEntry(this, rootWidget.getName());
		entries.put(rootWidget.getName(), root);
		this.selectedEntry = root.getName();
		this.selectedCollection = rootWidget.getCollector();
		this.window = rootWidget.getWindow();
	}

	public void addListener(ITreeListListener... listeners) {
		this.listeners.addAll(Arrays.asList(listeners));
	}

	public boolean selectEntry(String name) {
		WidgetTreeEntry entry = getEntry(name);
		if (name.equals(selectedEntry) || entry == null) {
			return false;
		}
		Optional<IWidget> widgetOptional = entry.getWidget();
		if (!widgetOptional.isPresent()) {
			return false;
		}
		WidgetTreeEntry oldEntry = entries.get(selectedEntry);
		selectedEntry = name;
		selectedCollection = widgetOptional.get().getCollector();
		for (ITreeListListener listener : listeners) {
			listener.onEntrySelection(this, entry, oldEntry);
		}
		return true;
	}

	public IPropertyCollection getSelectedCollection() {
		return selectedCollection;
	}

	public boolean isRootSelected() {
		WidgetTreeEntry entry = getSelectedEntry();
		return entry != null && entry.isRoot();
	}

	@Nullable
	public WidgetTreeEntry getSelectedEntry() {
		return entries.get(selectedEntry);
	}

	public Optional<IWidget> getSelectedWidget() {
		WidgetTreeEntry entry = getSelectedEntry();
		return entry == null ? Optional.empty() : entry.getWidget();
	}

	public boolean isSelected(String name) {
		return name.equals(selectedEntry);
	}

	public WidgetTreeEntry createEntry(IWidgetType type, boolean select) {
		WidgetTreeEntry entry = new WidgetTreeEntry(this, getNextName(), type);
		entries.put(entry.getName(), entry);
		root.addChild(entry);
		for (ITreeListListener listener : listeners) {
			listener.onEntryCreation(this, entry);
		}
		if (select) {
			selectEntry(entry.getName());
		}
		return entry;
	}

	private String getNextName() {
		for (int i = 0; i < entries.size() + 1; i++) {
			String name = String.format(DEFAULT_NAME, i);
			if (!entries.containsKey(name)) {
				return name;
			}
		}
		return String.format(DEFAULT_NAME, new Random().nextInt());
	}

	public void deleteEntry(@Nullable String name) {
		if (name == null) {
			name = selectedEntry;
		}
		WidgetTreeEntry entry = getEntry(name);
		if (entry == null) {
			return;
		}
		String parentName = entry.getParent();
		if (parentName == null) {
			return;
		}
		WidgetTreeEntry parent = getEntry(parentName);
		if (parent == null) {
			return;
		}
		for (ITreeListListener listener : listeners) {
			listener.onEntryDeletion(this, entry);
		}
		parent.removeChild(entry);
		entries.remove(name);
		selectEntry(root.getName());
	}

	public boolean renameEntry(String oldName, String newName) {
		if (!entries.containsKey(oldName) || entries.containsKey(newName)) {
			return false;
		}
		WidgetTreeEntry entry = entries.remove(oldName);
		if (entry == null || !entry.isValid()) {
			return false;
		}
		for (ITreeListListener listener : listeners) {
			listener.onEntryRename(this, entry, oldName, newName);
		}
		entry.setName(newName);
		entries.put(newName, entry);
		if (selectedEntry.equals(oldName)) {
			//selectEntry(newName);
			selectedEntry = oldName;
		}
		return true;
	}

	public boolean moveEntry(String entryName, String newParent) {
		WidgetTreeEntry entry = entries.get(entryName);
		WidgetTreeEntry parent = entries.get(newParent);
		if (entry == null || entry.getParent() == null || parent == null || !entry.isValid() || !parent.isValid() || !parent.getType().isContainer()) {
			return false;
		}
		WidgetTreeEntry currentParent = getEntry(entry.getParent());
		if (currentParent == null || !currentParent.isValid()) {
			return false;
		}
		currentParent.removeChild(entry);
		parent.addChild(entry);
		for (ITreeListListener listener : listeners) {
			listener.onEntryTransfer(this, entry, currentParent, parent);
		}
		return true;
	}

	public boolean hasEntry(String name) {
		return entries.containsKey(name);
	}

	@Nullable
	public WidgetTreeEntry getEntry(String name) {
		return entries.get(name);
	}

	public IWindowWidget getWindow() {
		return window;
	}

	public RootEntry getRoot() {
		return root;
	}

	public static class RootEntry extends WidgetTreeEntry {
		private RootEntry(WidgetTreeList treeList, String name) {
			super(treeList, name, WidgetType.EMPTY);
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public boolean isRoot() {
			return true;
		}
	}
}
