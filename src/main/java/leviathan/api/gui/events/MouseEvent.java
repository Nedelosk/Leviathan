package leviathan.api.gui.events;

import java.util.List;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWindowWidget;

/**
 * A event class that can be used for events that are related to the mouse.
 */
public class MouseEvent extends JEGEvent {
	public static final EventKey<MouseEvent> MOUSE_DOWN = new EventKey<>("mouse_down", MouseEvent.class);
	public static final EventKey<MouseEvent> MOUSE_UP = new EventKey<>("mouse_up", MouseEvent.class);
	public static final EventKey<MouseEvent> MOUSE_MOVE = new EventKey<>("mouse_move", MouseEvent.class);
	public static final EventKey<MouseEvent> MOUSE_ENTER = new EventKey<>("mouse_enter", MouseEvent.class);
	public static final EventKey<MouseEvent> MOUSE_LEAVE = new EventKey<>("mouse_leave", MouseEvent.class);
	public static final EventKey<MouseEvent> MOUSE_DRAG_START = new EventKey<>("mouse_drag_start", MouseEvent.class);
	public static final EventKey<MouseEvent> MOUSE_DRAG_END = new EventKey<>("mouse_drag_end", MouseEvent.class);
	public static final EventKey<MouseEvent> MOUSE_DRAG_MOVE = new EventKey<>("mouse_drag_move", MouseEvent.class);
	public static final EventKey<Wheel> WHEEL = new EventKey<>("mouse_wheel", Wheel.class);
	public static final EventKey<TooltipEvent> TOOLTIP = new EventKey<>("mouse_tooltip", TooltipEvent.class);

	//The position of the mouse on the x-axis.
	private final int positionX;
	//The position of the mouse on the y-axis.
	private final int positionY;
	//The position of the mouse on the x-axis relative to the position of the element.
	private final int relativeX;
	//The position of the mouse on the y-axis relative to the position of the element.
	private final int relativeY;
	//The clicked mouse button. Can be -1 if no button was clicked.
	private final int button;
	//The difference of the position of the mouse on the x-axis since the last event.
	private final float diffX;
	//The difference of the position of the mouse on the y-axis since the last event.
	private final float diffY;


	public MouseEvent(IWidget origin, EventKey key, int positionX, int positionY, int button) {
		this(origin, key, positionX, positionY, button, 0.0F, 0.0F);
	}

	public MouseEvent(IWidget origin, EventKey key, int positionX, int positionY, float diffX, float diffY) {
		this(origin, key, positionX, positionY, -1, diffX, diffY);
	}

	public MouseEvent(IWidget origin, EventKey key, int positionX, int positionY, int button, float diffX, float diffY) {
		super(origin, key);
		IWindowWidget window = origin.getWindow();
		this.positionX = positionX;
		this.positionY = positionY;
		this.button = button;
		this.relativeX = window.getRelativeMouseX(origin);
		this.relativeY = window.getRelativeMouseY(origin);
		this.diffX = diffX;
		this.diffY = diffY;
	}

	public int getButton() {
		return button;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public int getRelativeX() {
		return relativeX;
	}

	public int getRelativeY() {
		return relativeY;
	}

	public float getDiffX() {
		return diffX;
	}

	public float getDiffY() {
		return diffY;
	}

	public static class Wheel extends JEGEvent {
		private int dWheel;

		public Wheel(IWidget origin, int dWheel) {
			super(origin, WHEEL);
			this.dWheel = dWheel / 28;
		}

		public int getDWheel() {
			return this.dWheel;
		}
	}

	public static class TooltipEvent extends MouseEvent {
		private final List<String> lines;

		public TooltipEvent(IWidget origin, int positionX, int positionY, List<String> lines) {
			super(origin, TOOLTIP, positionX, positionY, -1);
			this.lines = lines;
		}

		public List<String> getLines() {
			return lines;
		}
	}
}
