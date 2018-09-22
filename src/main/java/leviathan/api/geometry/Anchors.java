package leviathan.api.geometry;

public class Anchors {
	public static final Anchors DEFAULT = new Anchors(0.0F, 0.0F, 0.0F, 0.0F);

	private final float left;
	private final float top;
	private final float right;
	private final float bottom;

	public Anchors(float left, float top, float right, float bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public Anchors(HorizontalAlignment horizontalAlign, VerticalAlignment verticalAlign) {
		this.left = this.right = horizontalAlign.getOffset();
		this.top = this.bottom = verticalAlign.getOffset();
	}

	public float getLeft() {
		return left;
	}

	public float getTop() {
		return top;
	}

	public float getRight() {
		return right;
	}

	public float getBottom() {
		return bottom;
	}
}
