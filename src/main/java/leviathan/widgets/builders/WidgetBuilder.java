package leviathan.widgets.builders;

import leviathan.api.geometry.Point;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IWidget;
import leviathan.api.widgets.builders.IWidgetBuilder;

@SuppressWarnings("unchecked")
public abstract class WidgetBuilder<W extends IWidget, B extends IWidgetBuilder> implements IWidgetBuilder<W, B> {
	private final IContainer container;
	protected String name;
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public WidgetBuilder(IContainer container, String name) {
		this(container, name, 0, 0, 0, 0);
	}

	public WidgetBuilder(IContainer container, String name, int x, int y, int width, int height) {
		this.container = container;
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public B setName(String name) {
		this.name = name;
		return (B) this;
	}

	@Override
	public B setPosition(Point position) {
		this.x = position.x;
		this.y = position.y;
		return (B) this;
	}

	@Override
	public B setSize(Point size) {
		this.width = size.x;
		this.height = size.y;
		return (B) this;
	}

	@Override
	public B setX(int x) {
		this.x = x;
		return (B) this;
	}

	@Override
	public B setY(int y) {
		this.y = y;
		return (B) this;
	}

	@Override
	public B setWidth(int width) {
		this.width = width;
		return (B) this;
	}

	@Override
	public B setHeight(int height) {
		this.height = height;
		return (B) this;
	}
}
