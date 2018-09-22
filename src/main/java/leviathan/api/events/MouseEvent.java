package leviathan.api.events;

import java.util.List;

import leviathan.api.geometry.Vector;
import leviathan.api.widgets.IWidget;

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

	//The position of the mouse.
	private final Vector position;
	//The position of the mouse relative to the position of the element.
	private final Vector relative;
	//The clicked mouse button. Can be -1 if no button was clicked.
	private final int button;
	//The difference of the position of the mouse since the last event.
	private final Vector delta;


	public MouseEvent(IWidget origin, EventKey key, Vector position, int button) {
		this(origin, key, position, button, Vector.ORIGIN);
	}

	public MouseEvent(IWidget origin, EventKey key, Vector position, Vector delta) {
		this(origin, key, position, -1, delta);
	}

	public MouseEvent(IWidget origin, EventKey key, Vector position, int button, Vector delta) {
		super(origin, key);
		this.position = position;
		this.button = button;
		this.relative = origin.positionRelativeToWidget(position);
		this.delta = delta;
	}

	public int getButton() {
		return button;
	}

	public Vector getPosition() {
		return position;
	}

	public Vector getRelative() {
		return relative;
	}

	public Vector getDelta() {
		return delta;
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

		public TooltipEvent(IWidget origin, Vector position, List<String> lines) {
			super(origin, TOOLTIP, position, -1);
			this.lines = lines;
		}

		public List<String> getLines() {
			return lines;
		}
	}
}
