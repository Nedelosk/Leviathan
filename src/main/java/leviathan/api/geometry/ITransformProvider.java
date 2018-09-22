package leviathan.api.geometry;

public interface ITransformProvider {
	RectTransform getTransform();

	default ITransformProvider setSize(Vector size){
		RectTransform transform = getTransform();
		transform.setSize(size);
		onTransformChange();
		return this;
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

	default ITransformProvider setPosition(Vector position){
		RectTransform transform = getTransform();
		transform.setPosition(position);
		onTransformChange();
		return this;
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

	Vector positionRelativeToWidget(Vector position);

	Vector positionOnScreen();
}
