package leviathan.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;

import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.JEGResourceType;
import org.jline.utils.Log;

@SideOnly(Side.CLIENT)
public class ColourProperties implements ISelectiveResourceReloadListener {

	public static final ColourProperties INSTANCE;

	static {
		INSTANCE = new ColourProperties();
	}

	private final Properties defaultMappings = new Properties();
	private final Properties mappings = new Properties();

	private ColourProperties() {
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}

	public synchronized IntSupplier getSupplier(String key) {
		return () -> Integer.parseInt(mappings.getProperty(key, defaultMappings.getProperty(key, "d67fff")), 16);
	}

	public synchronized int get(String key) {
		return Integer.parseInt(mappings.getProperty(key, defaultMappings.getProperty(key, "d67fff")), 16);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		if (!resourcePredicate.test(JEGResourceType.COLORS)) {
			return;
		}
		try {
			InputStream defaultFontStream = ColourProperties.class.getResourceAsStream("/config/forestry/colour.properties");
			mappings.load(defaultFontStream);
			defaultMappings.load(defaultFontStream);

			defaultFontStream.close();
		} catch (IOException e) {
			Log.error("Failed to load colors.properties.", e);
		}
	}

}
