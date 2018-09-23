package leviathan.api.geometry;

public interface ITransformProvider {
	RectTransform getTransform();

	default ITransformProvider setSize(Point size) {
		RectTransform transform = getTransform();
		transform.setSize(size);
		onTransformChange();
		return this;
	}

	default Point getSize() {
		return getTransform().getSize();
	}

	default ITransformProvider setWidth(int width) {
		return setSize(getTransform().getPosition().withX(width));
	}

	default ITransformProvider setHeight(int height) {
		return setSize(getTransform().getPosition().withY(height));
	}

	default int getWidth() {
		return getSize().getX();
	}

	default int getHeight() {
		return getSize().getY();
	}

	default ITransformProvider setSizeDelta(Vector size){
		RectTransform transform = getTransform();
		transform.setSizeDelta(size);
		onTransformChange();
		return this;
	}

	default ITransformProvider setScale(Vector scale){
		RectTransform transform = getTransform();
		transform.setScale(scale);
		onTransformChange();
		return this;
	}

	default ITransformProvider setPosition(Point position) {
		RectTransform transform = getTransform();
		transform.setPosition(position);
		onTransformChange();
		return this;
	}

	default Point getPosition() {
		return getTransform().getPosition();
	}

	default int getX() {
		return getPosition().x;
	}

	default int getY() {
		return getPosition().y;
	}

	default ITransformProvider setX(int x) {
		return setPosition(getTransform().getPosition().withX(x));
	}

	default ITransformProvider setY(int y) {
		return setPosition(getTransform().getPosition().withY(y));
	}

	default ITransformProvider setAnchors(Anchors anchors){
		RectTransform transform = getTransform();
		transform.setAnchors(anchors);
		onTransformChange();
		return this;
	}

	default ITransformProvider setAlignment(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment){
		return setAnchors(new Anchors(horizontalAlignment, verticalAlignment));
	}

	void onTransformChange();

	void onTransformParentChange();

	Point positionRelativeToWidget(Point position);

	Point positionOnScreen();
}
