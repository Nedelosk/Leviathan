package leviathan.widgets;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import leviathan.api.ILayoutManager;
import leviathan.api.events.ElementEvent;
import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;
import leviathan.api.text.GuiConstants;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IWidget;
import leviathan.api.widgets.builders.IImageBuilder;
import leviathan.api.widgets.builders.ILabelBuilder;
import leviathan.widgets.builders.ImageBuilder;
import leviathan.widgets.builders.LabelBuilder;

public class Container extends Widget implements IContainer {
	protected final Map<String, IWidget> children = new HashMap<>();
	@Nullable
	private ILayoutManager layoutManager;

	public Container(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
		addListener(ElementEvent.RENAME, event -> {
			children.put(event.getNewName(), children.remove(event.getOldName()));
		});
	}

	@Override
	public void add(Collection<IWidget> widgets) {
		widgets.forEach(widget -> {
			children.put(widget.getName(), widget);
			widget.onCreation();
		});
	}

	@Override
	public void remove(Collection<IWidget> widgets) {
		widgets.forEach(widget -> {
			widget.onDeletion();
			children.remove(widget.getName());
		});
	}

	@Override
	public Collection<IWidget> getChildren() {
		return children.values();
	}

	@Override
	public boolean hasWidget(String name) {
		return children.containsKey(name);
	}

	@Override
	public Optional<IWidget> getWidget(String name) {
		return Optional.ofNullable(children.get(name));
	}

	@Override
	public Optional<IWidget> getWidgetRecursively(String name) {
		Optional<IWidget> optionalWidget = getWidget(name);
		return optionalWidget.isPresent() ? optionalWidget : getContainer().flatMap(container -> container.getWidgetRecursively(name));
	}

	@Override
	public ILabelBuilder label(String name, String text) {
		return new LabelBuilder(this, name, text, GuiConstants.DEFAULT_STYLE);
	}

	@Override
	public IImageBuilder image(String name, IDrawable drawable) {
		return new ImageBuilder(this, name, drawable);
	}

	@Override
	public IImageBuilder image(String name, ISprite sprite) {
		return new ImageBuilder(this, name, sprite);
	}

	@Override
	protected void drawWidget() {
		super.drawWidget();
		getChildren().forEach(IWidget::draw);
	}

	@Override
	protected void onClientUpdate() {
		getChildren().forEach(IWidget::updateClient);
	}

	@Override
	protected void doLayout() {
		if (layoutManager != null) {
			layoutManager.layoutWidget(this);
		}
		for (IWidget child : getChildren()) {
			child.validate();
		}
	}

	@Override
	protected void onNameChange(String oldName, String newName) {
		super.onNameChange(oldName, newName);
		getChildren().forEach(widget -> widget.onParentNameChange(oldName, newName));
	}

	public void setLayoutManager(@Nullable ILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}

	@Nullable
	public ILayoutManager getLayoutManager() {
		return layoutManager;
	}
}
