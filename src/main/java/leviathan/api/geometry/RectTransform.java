package leviathan.api.geometry;

import javax.annotation.Nullable;
import java.awt.Rectangle;

import leviathan.api.widgets.IWidget;

public class RectTransform {
	public static final Vector DEFAULT_SCALE = new Vector(1.0F, 1.0F);
	public static final Point DEFAULT_SIZE = new Point(32, 32);

	public static RectTransform createEmpty(IWidget widget){
		return new RectTransform(widget, 0, 0, 0, 0);
	}

	private final IWidget widget;

	private Anchors anchors;
	private Point position;
	private Vector sizeDelta;
	private Vector scale;
	@Nullable
	private Point screenPosition;
	@Nullable
	private RectTransform parent;
	@Nullable
	private RectTransform cropTransform;

	public RectTransform(IWidget widget, Region region) {
		this(widget, region.getPosition(), region.getSize(), Anchors.DEFAULT, DEFAULT_SCALE);
	}

	public RectTransform(IWidget widget, int width, int height) {
		this(widget, 0, 0, width, height);
	}

	public RectTransform(IWidget widget, int x, int y, int width, int height) {
		this(widget, x, y, width, height, 1.0F, 1.0F);
	}

	public RectTransform(IWidget widget, int x, int y, int width, int height, float scaleX, float scaleY) {
		this(widget, new Point(x, y), new Point(width, height), Anchors.DEFAULT, new Vector(scaleX, scaleY));
	}

	public RectTransform(IWidget widget, Point position, Point size, Anchors anchors, Vector scale) {
		this.widget = widget;
		this.position = position;
		this.sizeDelta = size.toVec();
		this.anchors = anchors;
		this.scale = scale;
	}

	public void setPosition(Point position) {
		this.position = position;
		this.screenPosition = null;
		widget.onTransformChange();
	}

	public Point getPosition() {
		return position;
	}

	public void setSizeDelta(Vector sizeDelta) {
		this.sizeDelta = sizeDelta;
	}

	public Vector getSizeDelta() {
		return sizeDelta;
	}

	public void setSize(Point size) {
		this.sizeDelta = Vector.divide(size, scale);
		this.screenPosition = null;
		widget.onTransformChange();
	}

	public Point getSize() {
		return Vector.multiply(sizeDelta, getAbsoluteScale()).roundPoint();
	}

	public Vector getAbsoluteScale() {
		return Vector.multiply(scale, (parent != null ? parent.getAbsoluteScale() : Vector.RIGHT_UP));
	}

	public Point getScreenPosition() {
		if(screenPosition == null){
			Point scaledPosition = getPosition().copy();
			if(parent != null){
				Point parentPosition = parent.getScreenPosition();
				screenPosition = Point.add(scaledPosition, parentPosition);
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

	public boolean contains(Point position) {
		return intersects(position, Point.RIGHT_UP);
	}

	public boolean intersects(Point position, Point size) {
		return intersects(position.getX(), position.getY(), size.getX(), size.getY());
	}

	public boolean intersects(int x, int y, int width, int height) {
		Point size = getSize();
		Point pos = getPosition();
		int w = size.getX();
		int h = size.getY();
		int pX = pos.getX();
		int pY = pos.getY();
		if(cropTransform != null){
			Point cropPos = Point.sub(new Point(x, y), Point.sub(cropTransform.getPosition(), pos));
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
		Point size = getSize();
		Point pos = getPosition();
		return new Rectangle(pos.getX(), pos.getY(), size.getX(), size.getY());
	}
}
