package leviathan.api.events;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.geometry.Region;
import leviathan.api.gui.IWidget;

@SideOnly(Side.CLIENT)
public class ElementEvent extends JEGEvent {
	public static final EventKey<RegionChange> REGION_CHANGE = new EventKey<>("region_change", RegionChange.class);
	public static final EventKey<ElementEvent> CREATION = new EventKey<>("creation", ElementEvent.class);
	public static final EventKey<ElementEvent> DELETION = new EventKey<>("delection", ElementEvent.class);
	public static final EventKey<Rename> RENAME = new EventKey<>("rename", Rename.class);

	public ElementEvent(IWidget origin, EventKey key) {
		super(origin, key);
	}

	public static class RegionChange extends ElementEvent {
		private final Region oldRegion;
		private final Region newRegion;

		public RegionChange(IWidget origin, Region oldRegion, Region newRegion) {
			super(origin, REGION_CHANGE);
			this.oldRegion = oldRegion;
			this.newRegion = newRegion;
		}

		public Region getOldRegion() {
			return oldRegion;
		}

		public Region getNewRegion() {
			return newRegion;
		}
	}

	public static class Rename extends ElementEvent {
		private final String oldName;
		private final String newName;
		private boolean canceled = false;

		public Rename(IWidget origin, String oldName, String newName) {
			super(origin, RENAME);
			this.oldName = oldName;
			this.newName = newName;
		}

		public String getOldName() {
			return oldName;
		}

		public String getNewName() {
			return newName;
		}

		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}

		public boolean isCanceled() {
			return canceled;
		}
	}
}
