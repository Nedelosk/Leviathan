package leviathan.utils;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.gui.Constants;
import leviathan.proxy.I18NProxy;

public class I18N {
	@Nullable
	private static NumberFormat percentFormat;
	@Nullable
	private static NumberFormat numberFormat;

	public static void onResourceReload() {
		percentFormat = null;
		numberFormat = null;
	}

	public static NumberFormat getPercentFormat() {
		if (percentFormat == null) {
			percentFormat = DecimalFormat.getPercentInstance(getLocale());
		}
		return percentFormat;
	}

	public static NumberFormat getNumberFormat() {
		if (numberFormat == null) {
			numberFormat = DecimalFormat.getNumberInstance(I18N.getLocale());
		}
		return numberFormat;
	}

	public static String localiseOrBlank(String key) {
		return proxy.localiseOrBlank(key);
	}

	public static String localise(String key) {
		return proxy.localise(key);
	}

	public static String localise(ResourceLocation key) {
		return proxy.localise(key);
	}

	public static boolean canLocalise(String key) {
		return proxy.canLocalise(key);
	}

	public static String localise(String key, Object... format) {
		return proxy.localise(key, format);
	}

	public static String localise(ResourceLocation key, Object... format) {
		return proxy.localise(key, format);
	}

	@SuppressWarnings("NullableProblems")
	@SidedProxy(clientSide = "leviathan.proxy.I18NClient", serverSide = "leviathan.proxy.I18NServer", modId = Constants.MOD_ID)
	private static I18NProxy proxy;

	@SuppressWarnings("ConstantConditions")
	@SideOnly(Side.CLIENT)
	public static Locale getLocale() {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft != null) {
			LanguageManager languageManager = minecraft.getLanguageManager();
			if (languageManager != null) {
				Language currentLanguage = languageManager.getCurrentLanguage();
				if (currentLanguage != null) {
					return currentLanguage.getJavaLocale();
				}
			}
		}
		return Locale.getDefault();
	}
}
