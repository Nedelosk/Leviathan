package leviathan.api.events;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IWidget;
import leviathan.utils.Log;


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
			if (!(element instanceof IContainer)) {
				return;
			}
			for (IWidget child : ((IContainer) element).getChildren()) {
				child.receiveEvent(event);
			}
		}
	},
	//The parent element of the element
	PARENT {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			element.getContainer().ifPresent(widget -> widget.receiveEvent(event));
		}
	},
	//The other children of the parent of the element
	SIBLINGS {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			element.getContainer().ifPresent(widget -> widget.dispatchEvent(event, CHILDREN));
		}
	},
	ALL {
		@Override
		public void sendEvent(IWidget element, JEGEvent event) {
			if(event.getEventKey() == MouseEvent.MOUSE_DOWN){
				Log.error("---------------------------------------------------------");
				Log.error("Dispatched " + event.getEventKey() + " to the widget " + element.getName() + " at " + element.positionOnScreen() + " from the widget " + event.getSource().getName() + " at " + event.getSource().positionOnScreen());
				Log.error("---------------------------------------------------------");
			}
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
