package leviathan.utils;

import net.minecraft.util.ResourceLocation;

import leviathan.gui.Constants;

public class ResourceUtil {
	private ResourceUtil() {
	}

	public static ResourceLocation location(String domain, String path) {
		return new ResourceLocation(domain, path);
	}

	public static ResourceLocation location(String path) {
		return location(Constants.MOD_ID, path);
	}

	public static ResourceLocation guiLocation(String path) {
		return location(Constants.TEXTURE_PATH_GUI + "/" + path);
	}
}
