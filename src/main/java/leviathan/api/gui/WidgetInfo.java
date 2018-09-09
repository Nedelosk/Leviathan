package leviathan.api.gui;

import leviathan.api.Region;

public class WidgetInfo {
	private Region region;
	private String name;
	private String type;

	public WidgetInfo(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public boolean isContainer() {
		return false;
	}
}
