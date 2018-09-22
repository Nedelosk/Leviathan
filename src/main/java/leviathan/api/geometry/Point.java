package leviathan.api.geometry;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public final class Point {
	public static final Point ORIGIN = new Point(0, 0);

	private final int x;
	private final int y;

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
