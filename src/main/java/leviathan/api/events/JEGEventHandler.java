package leviathan.api.events;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.IWidget;

@SideOnly(Side.CLIENT)
public final class JEGEventHandler<E extends JEGEvent> implements Consumer<E> {
	private final Consumer<E> handlerAction;
	private final Class<? super E> eventClass;
	private final EventOrigin origin;
	@Nullable
	private final IWidget relative;

	public JEGEventHandler(Class<? super E> eventClass, Consumer<E> handlerAction) {
		this.origin = EventOrigin.ANY;
		this.relative = null;
		this.eventClass = eventClass;
		this.handlerAction = handlerAction;
	}

	public JEGEventHandler(Class<? super E> eventClass, EventOrigin origin, IWidget relative, Consumer<E> handlerAction) {
		this.origin = origin;
		this.relative = relative;
		this.eventClass = eventClass;
		this.handlerAction = handlerAction;
	}

	private boolean canHandle(JEGEvent event) {
		boolean instance = this.eventClass.isInstance(event);
		return instance && this.origin.isOrigin(event.getSource(), this.relative);
	}

	@Override
	public final void accept(E e) {
		if (canHandle(e)) {
			handlerAction.accept(e);
		}
	}
}
