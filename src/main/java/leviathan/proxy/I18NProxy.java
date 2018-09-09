package leviathan.proxy;

import net.minecraft.util.ResourceLocation;


public interface I18NProxy {
	String localiseOrBlank(String key);

	String localise(String key);

	String localise(ResourceLocation key);

	boolean canLocalise(String key);

	String localise(String key, Object... format);

	String localise(ResourceLocation key, Object... format);
}
