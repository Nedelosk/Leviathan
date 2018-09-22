package leviathan.api.geometry;

public enum HorizontalAlignment {
	LEFT, CENTER, RIGHT;

	public float getOffset(){
		return ordinal() * 0.5F;
	}
}
