package leviathan.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import leviathan.api.ILayoutManager;
import leviathan.api.widgets.IContainer;
import leviathan.api.widgets.IWidget;

public class Container extends Widget implements IContainer {
	private final List<IWidget> children = new ArrayList<>();
	private ILayoutManager layoutManager;

	public Container(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
	}

	@Override
	public void add(Collection<IWidget> widgets) {
		children.addAll(widgets);
	}

	@Override
	public void remove(Collection<IWidget> widgets) {
		children.removeAll(widgets);
	}

	@Override
	public Collection<IWidget> getChildren() {
		return children;
	}

	@Override
	protected void drawWidget() {
		super.drawWidget();
		children.forEach(IWidget::draw);
	}

	@Override
	protected void onClientUpdate() {
		children.forEach(IWidget::updateClient);
	}

	@Override
	protected void doLayout() {
		layoutManager.layoutWidget(this);
		for(IWidget child : children){
			child.validate();
		}
	}

	public void setLayoutManager(ILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}

	public ILayoutManager getLayoutManager() {
		return layoutManager;
	}
}
