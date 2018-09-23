package leviathan.api.geometry;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public final class Point {
	public static final Point ORIGIN = new Point(0, 0);
	public static final Point LEFT = new Point(-1, 0);
	public static final Point RIGHT = new Point(1, 0);
	public static final Point DOWN = new Point(0, -1);
	public static final Point UP = new Point(0, 1);
	public static final Point RIGHT_UP = new Point(1, 1);

	public final int x;
	public final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public Point translate(Point point){
		return translate(point.getX(), point.getY());
	}

	public Point subtract(Point point){
		return translate(-point.getX(), -point.getY());
	}

	public Point translate(int x, int y){
		return new Point(this.x + x, this.y + y);
	}

	/**
	 * @return Creates a region from the coordinate origin to this point of the coordinate system.
	 */
	public Region toRegion(){
		return new Region(0, 0, x, y);
	}

	public Point withX(int x) {
		return new Point(x, y);
	}

	public Point withY(int y) {
		return new Point(x, y);
	}

	public static Point add(Point left, Point right) {
		return new Point(left.x + right.x, left.y + right.y);
	}

	public static Point sub(Point left, Point right) {
		return new Point(left.x - right.x, left.y - right.y);
	}

	public static Point multiply(Point left, Point right) {
		return new Point(left.x * right.x, left.y * right.y);
	}

	public static Point divide(Point dividend, Point divisor) {
		return new Point(dividend.x / divisor.x, dividend.y / divisor.y);
	}

	public Point negate() {
		return new Point(-x, -y);
	}

	public Point copy() {
		return new Point(x, y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Point point = (Point) o;
		return x == point.x &&
			y == point.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "Point{x=" + x + ", y=" + y + '}';
	}

	public Vector toVec() {
		return new Vector(x, y);
	}
}
