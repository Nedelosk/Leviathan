package leviathan.api.gui.events;

import javax.annotation.Nullable;
import java.util.Collection;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetGroup;

@SideOnly(Side.CLIENT)
public enum EventOrigin {
	ANY {
		@Override
		public boolean isOrigin(IWidget origin, @Nullable IWidget element) {
			return true;
		}
	},
	SELF {
		@Override
		public boolean isOrigin(IWidget origin, @Nullable IWidget element) {
			return element == origin;
		}
	},
	PARENT {
		@Override
		public boolean isOrigin(IWidget origin, @Nullable IWidget element) {
			return element != null && element.getParent() == origin;
		}
	},
	DIRECT_CHILD {
		@Override
		public boolean isOrigin(IWidget origin, @Nullable IWidget element) {
			if (element == null || !(element instanceof IWidgetGroup)) {
				return false;
			}
			Collection<IWidget> elements = ((IWidgetGroup) element).getElements();
			return elements.contains(origin);
		}
	};

	public abstract boolean isOrigin(IWidget origin, @Nullable IWidget element);
}
