package leviathan.api.geometry;

import javax.annotation.Nullable;
import java.util.Objects;

public class Vector {
	public static final Vector ORIGIN = new Vector(0, 0);
	public static final Vector LEFT = new Vector(-1, 0);
	public static final Vector RIGHT = new Vector(1, 0);
	public static final Vector DOWN = new Vector(0, -1);
	public static final Vector UP = new Vector(0, 1);
	public static final Vector RIGHT_UP = new Vector(1, 1);

	public float x;
	public float y;

	/**
	 * Constructor for Vector3f.
	 */
	public Vector() {
		super();
	}

	/**
	 * Constructor
	 */
	public Vector(Vector src) {
		set(src);
	}

	/**
	 * Constructor
	 */
	public Vector(float x, float y) {
		set(x, y);
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Load from another Vector2f
	 * @param src The source vector
	 * @return this
	 */
	public Vector set(Vector src) {
		x = src.getX();
		y = src.getY();
		return this;
	}

	/**
	 * @return the length squared of the vector
	 */
	public float lengthSquared() {
		return x * x + y * y;
	}

	/**
	 * Translate a vector
	 * @param x The translation in x
	 * @param y the translation in y
	 * @return this
	 */
	public Vector translate(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Negate a vector
	 * @return this
	 */
	public Vector negate() {
		x = -x;
		y = -y;
		return this;
	}

	/**
	 * Negate a vector and place the result in a destination vector.
	 * @param dest The destination vector or null if a new vector is to be created
	 * @return the negated vector
	 */
	public Vector negate(@Nullable Vector dest) {
		if (dest == null)
			dest = new Vector();
		dest.x = -x;
		dest.y = -y;
		return dest;
	}


	/**
	 * Normalise this vector and place the result in another vector.
	 * @param dest The destination vector, or null if a new vector is to be created
	 * @return the normalised vector
	 */
	public Vector normalise(@Nullable Vector dest) {
		float l = length();

		if (dest == null)
			dest = new Vector(x / l, y / l);
		else
			dest.set(x / l, y / l);

		return dest;
	}

	/**
	 * @return the length of the vector
	 */
	public final float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	/**
	 * The dot product of two vectors is calculated as
	 * v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
	 * @param left The LHS vector
	 * @param right The RHS vector
	 * @return left dot right
	 */
	public static float dot(Vector left, Vector right) {
		return left.x * right.x + left.y * right.y;
	}



	/**
	 * Calculate the angle between two vectors, in radians
	 * @param a A vector
	 * @param b The other vector
	 * @return the angle between the two vectors, in radians
	 */
	public static float angle(Vector a, Vector b) {
		float dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1f)
			dls = -1f;
		else if (dls > 1.0f)
			dls = 1.0f;
		return (float)Math.acos(dls);
	}

	/**
	 * Add a vector to another vector and place the result in a destination
	 * vector.
	 * @param left The LHS vector
	 * @param right The RHS vector
	 * @param dest The destination vector, or null if a new vector is to be created
	 * @return the sum of left and right in dest
	 */
	public static Vector add(Vector left, Vector right, @Nullable Vector dest) {
		if (dest == null)
			return new Vector(left.x + right.x, left.y + right.y);
		else {
			dest.set(left.x + right.x, left.y + right.y);
			return dest;
		}
	}

	public static Vector add(Vector left, Vector right) {
		return add(left, right, null);
	}

	public static Vector add(Point left, Vector right) {
		return add(left.toVec(), right, null);
	}

	/**
	 * Subtract a vector from another vector and place the result in a destination
	 * vector.
	 * @param left The LHS vector
	 * @param right The RHS vector
	 * @param dest The destination vector, or null if a new vector is to be created
	 * @return left minus right in dest
	 */
	public static Vector sub(Vector left, Vector right, @Nullable Vector dest) {
		if (dest == null)
			return new Vector(left.x - right.x, left.y - right.y);
		else {
			dest.set(left.x - right.x, left.y - right.y);
			return dest;
		}
	}

	public static Vector sub(Vector left, Vector right) {
		return sub(left, right, null);
	}

	public static Vector multiply(Vector left, Vector right, @Nullable Vector dest) {
		if (dest == null)
			return new Vector(left.x * right.x, left.y * right.y);
		else {
			dest.set(left.x * right.x, left.y * right.y);
			return dest;
		}
	}

	public static Vector multiply(Vector left, Vector right) {
		return multiply(left, right, null);
	}

	public static Vector divide(Vector dividend, Vector divisor, @Nullable Vector dest){
		if (dest == null)
			return new Vector(dividend.x / divisor.x, dividend.y / divisor.y);
		else {
			dest.set(dividend.x / divisor.x, dividend.y / divisor.y);
			return dest;
		}
	}

	public static Vector divide(Vector dividend, Vector divisor){
		return  divide(dividend, divisor, null);
	}

	public static Vector divide(Point dividend, Vector divisor) {
		return divide(dividend.toVec(), divisor, null);
	}

	public Vector scale(float scale) {
		x *= scale;
		y *= scale;

		return this;
	}

	public Vector copy(){
		return new Vector(x, y);
	}

	public Point toPoint() {
		return new Point((int) x, (int) y);
	}

	public Point roundPoint() {
		return new Point(Math.round(x), Math.round(y));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(64);

		sb.append("Vector[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(']');
		return sb.toString();
	}

	/**
	 * @return x
	 */
	public final float getX() {
		return x;
	}

	/**
	 * @return y
	 */
	public final float getY() {
		return y;
	}

	/**
	 * Set X
	 * @param x
	 */
	public final void setX(float x) {
		this.x = x;
	}

	/**
	 * Set Y
	 * @param y
	 */
	public final void setY(float y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Vector vector = (Vector) o;
		return Float.compare(vector.x, x) == 0 &&
			Float.compare(vector.y, y) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
