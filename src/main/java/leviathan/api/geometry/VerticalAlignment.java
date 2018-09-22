package leviathan.api.geometry;

public enum VerticalAlignment {
	TOP, MIDDLE, BOTTOM;

	public float getOffset(){
		return ordinal() * 0.5F;
	}
}
