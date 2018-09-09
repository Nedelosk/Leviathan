package leviathan;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.registries.RegistryBuilder;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import leviathan.api.gui.IWidgetType;
import leviathan.gui.Constants;
import leviathan.proxy.CommonProxy;

@Mod(modid = Constants.MOD_ID, name = Constants.NAME)
public class LeviathanMod {
	@SuppressWarnings("NullableProblems")
	@Mod.Instance(Constants.MOD_ID)
	private static LeviathanMod instance;
	@SuppressWarnings("NullableProblems")
	@SidedProxy(clientSide = "leviathan.proxy.ClientProxy", serverSide = "leviathan.proxy.ServerProxy")
	private static CommonProxy proxy;

	private static Configuration config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		new RegistryBuilder<IWidgetType>().setType(IWidgetType.class).setName(new ResourceLocation(Constants.MOD_ID, "widget_types")).create();
		//ASMEventHandler
		//event.getAsmData().getAll(Property.class.getName()).forEach(asmData -> asmData.);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

}
