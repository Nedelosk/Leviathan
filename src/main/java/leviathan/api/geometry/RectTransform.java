package leviathan.api.geometry;

import javax.annotation.Nullable;

import java.awt.Rectangle;

import leviathan.api.widgets.IWidget;

public class RectTransform {
	public static final Vector DEFAULT_SCALE = new Vector(1.0F, 1.0F);
	public static final Vector DEFAULT_SIZE = new Vector(32, 32);

	public static RectTransform createEmpty(IWidget widget){
		return new RectTransform(widget, 0, 0, 0, 0);
	}

	private final IWidget widget;

	private Anchors anchors;
	private Vector position;
	private Vector sizeDelta;
	private Vector scale;
	@Nullable
	private Vector screenPosition;
	@Nullable
	private RectTransform parent;
	@Nullable
	private RectTransform cropTransform;

	public RectTransform(IWidget widget, Region region) {
		this(widget, region.getPosition().toVec(), region.getSize().toVec(), Anchors.DEFAULT, DEFAULT_SCALE);
	}

	public RectTransform(IWidget widget, float width, float height){
		this(widget, 0, 0, width, height);
	}

	public RectTransform(IWidget widget, float x, float y, float width, float height){
		this(widget, x, y, width, height, 1.0F, 1.0F);
	}

	public RectTransform(IWidget widget, float x, float y, float width, float height, float scaleX, float scaleY){
		this(widget, new Vector(x, y), new Vector(width, height), Anchors.DEFAULT, new Vector(scaleX, scaleY));
	}

	public RectTransform(IWidget widget, Vector position, Vector size, Anchors anchors, Vector scale) {
		this.widget = widget;
		this.position = position;
		this.sizeDelta = size;
		this.anchors = anchors;
		this.scale = scale;
	}

	public void setPosition(Vector position) {
		this.position = position;
		this.screenPosition = null;
		widget.onTransformChange();
	}

	public Vector getPosition() {
		return position;
	}

	public void setSizeDelta(Vector sizeDelta) {
		this.sizeDelta = sizeDelta;
	}

	public Vector getSizeDelta() {
		return sizeDelta;
	}

	public void setSize(Vector size){
		this.sizeDelta = Vector.divide(size, scale);
		this.screenPosition = null;
		widget.onTransformChange();
	}

	public Vector getSize(){
		return Vector.multiply(sizeDelta, getAbsoluteScale());
	}

	public Vector getAbsoluteScale() {
		return Vector.multiply(scale, (parent != null ? parent.getAbsoluteScale() : Vector.RIGHT_UP));
	}

	public Vector getScreenPosition() {
		if(screenPosition == null){
			Vector scaledPosition = getPosition().copy();
			if(parent != null){
				Vector parentPosition = parent.getScreenPosition();
				screenPosition = Vector.add(scaledPosition, parentPosition);
			}else {
				screenPosition = scaledPosition;
			}
		}
		return screenPosition;
	}

	public void setCropTransform(@Nullable RectTransform cropTransform) {
		this.cropTransform = cropTransform;
	}

	@Nullable
	public RectTransform getCropTransform() {
		return cropTransform;
	}

	public Anchors getAnchors() {
		return anchors;
	}

	public void setAnchors(Anchors anchors) {
		this.anchors = anchors;
		this.screenPosition = null;
		widget.onTransformChange();
	}

	public Vector getScale() {
		return scale;
	}

	public void setScale(Vector scale) {
		this.scale = scale;
		this.screenPosition = null;
		widget.onTransformChange();
	}

	@Nullable
	public RectTransform getParent() {
		return parent;
	}

	public void setParent(@Nullable RectTransform parent) {
		this.parent = parent;
		this.screenPosition = null;
		widget.onTransformParentChange();
	}

	public IWidget getWidget() {
		return widget;
	}

	public boolean contains(Point point){
		return contains(new Vector(point.getX(), point.getY()));
	}

	public boolean contains(Vector position){
		return intersects(position, Vector.RIGHT_UP);
	}

	public boolean intersects(Vector position, Vector size) {
		return intersects(position.x, position.y, size.x, size.y);
	}

	public boolean intersects(float x, float y, float width, float height) {
		Vector size = getSize();
		Vector pos = getPosition();
		float w = size.x;
		float h = size.y;
		float pX = pos.x;
		float pY = pos.y;
		if(cropTransform != null){
			Vector cropPos = Vector.sub(new Vector(x, y), Vector.sub(cropTransform.getPosition(), pos));
			return cropTransform.contains(cropPos);
		}
		//No dimension
		if (width <= 0 || height <= 0 || w <= 0 ||h <= 0) {
			return false;
		}

		//Overflow or intersect
		return ((x + width < x || x + width > pX)
			&& (y + height < y || y + height > pY)
			&& (pX + w < pX || pX + w > x)
			&& (pY + h < pY || pY + h > y));
	}

	public Rectangle toRectangle() {
		Vector size = getSize();
		Vector pos = getPosition();
		return new Rectangle(Math.round(pos.x), Math.round(pos.y), Math.round(size.x), Math.round(size.y));
	}
}
