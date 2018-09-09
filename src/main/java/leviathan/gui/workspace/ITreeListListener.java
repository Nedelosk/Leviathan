package leviathan.gui.workspace;

import javax.annotation.Nullable;

public interface ITreeListListener {
	default void onEntryCreation(WidgetTreeList treeList, WidgetTreeEntry entry) {
	}

	default void onEntryDeletion(WidgetTreeList treeList, WidgetTreeEntry entry) {

	}

	default void onEntryRename(WidgetTreeList treeList, WidgetTreeEntry entry, String oldName, String newName) {

	}

	default void onEntryTransfer(WidgetTreeList treeList, WidgetTreeEntry entry, WidgetTreeEntry oldParent, WidgetTreeEntry newParent) {

	}

	default void onEntrySelection(WidgetTreeList treeList, WidgetTreeEntry entry, @Nullable WidgetTreeEntry oldEntry) {
	}
}
