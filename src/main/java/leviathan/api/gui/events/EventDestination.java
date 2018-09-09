package leviathan.api.gui.events;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWidgetGroup;


@SideOnly(Side.CLIENT)
public enum EventDestination {
	//Only the current element
	SINGLE {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			element.receiveEvent(event);
		}
	},
	//Only the origin element
	ORIGIN {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			event.getSource().receiveEvent(event);
		}
	},
	//All children elements of the element
	CHILDREN {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			if (!(element instanceof IWidgetGroup)) {
				return;
			}
			for (IWidget child : ((IWidgetGroup) element).getElements()) {
				child.receiveEvent(event);
			}
		}
	},
	//The parent element of the element
	PARENT {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			IWidget parent = element.getParent();
			if (parent == null) {
				return;
			}
			parent.receiveEvent(event);
		}
	},
	//The other children of the parent of the element
	SIBLINGS {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			IWidget parent = element.getParent();
			if (parent == null) {
				return;
			}
			parent.dispatchEvent(event, CHILDREN);
		}
	},
	ALL {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			element.receiveEvent(event);
			/*if(!(element instanceof IWidgetGroup)){
				return;
			}
			for(IWidget child : ((IWidgetGroup) element).getElements()){
				child.dispatchEvent(event, ALL);
			}*/
		}
	};

	public abstract void sendEvent(IWidget element, JEGEvent event);
}
