package leviathan.api.geometry;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public final class Region {
	public static final Region EMPTY = new Region(0, 0, 0, 0);
	public static final Region DEFAULT = new Region(0, 0, 16, 16);

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	
	public Region(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point getPosition(){
		return new Point(x, y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Point getSize(){
		return new Point(width, height);
	}

	public boolean isEmpty() {
		return width <= 0 || height <= 0;
	}

	public Region withSize(int width, int height) {
		return new Region(x, y, width, height);
	}

	public Region withSize(Point size) {
		return new Region(x, y, size.getX(), size.getY());
	}

	public Region withPosition(Point position) {
		return new Region(position.getX(), position.getY(), width, height);
	}

	public Region withPosition(int xPos, int yPos) {
		return new Region(xPos, yPos, width, height);
	}

	public Region scale(float xScale, float yScale){
		return new Region(x, y, Math.round(width * xScale), Math.round(height * yScale));
	}

	public Region translate(Point point) {
		return new Region(x + point.getX(), y + point.getY(), width, height);
	}

	public Region translate(int xPos, int yPos) {
		return new Region(x + xPos, y + yPos, width, height);
	}

	public Region grow(int width, int height) {
		return new Region(x, y, this.width + width, this.height + height);
	}

	public Region shrink(int width, int height) {
		return new Region(x, y, this.width - width, this.height - height);
	}

	public boolean contains(int x, int y) {
		return intersects(x, y, 1, 1);
	}

	public boolean contains(Point point){
		return intersects(point.getX(), point.getY(), 1, 1);
	}

	public boolean intersects(Region region) {
		return intersects(region.x, region.y, region.width, region.height);
	}

	public boolean intersects(int x, int y, int width, int height) {
		//No dimension
		if (width <= 0 || height <= 0 || this.width <= 0 || this.height <= 0) {
			return false;
		}

		//Overflow or intersect
		return ((x + width < x || x + width > this.x)
			&& (y + height < y || y + height > this.y)
			&& (this.x + this.width < this.x || this.x + this.width > x)
			&& (this.y + this.height < this.y || this.y + this.height > y));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Region region = (Region) o;
		return x == region.x && y == region.y && width == region.width && height == region.height;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, width, height);
	}

	@Override
	public String toString() {
		return width + "x" + height + '@' + x + ',' + y;
	}
}
