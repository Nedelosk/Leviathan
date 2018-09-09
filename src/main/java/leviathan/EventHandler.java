package leviathan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import leviathan.api.WidgetTab;
import leviathan.api.gui.GuiConstants;
import leviathan.api.gui.IWidgetType;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.gui.WidgetType;
import leviathan.gui.widget.ColoredWidget;
import leviathan.gui.widget.ItemWidget;
import leviathan.gui.widget.LabelWidget;
import leviathan.gui.widget.TextEditWidget;
import leviathan.gui.widget.TextureWidget;
import leviathan.gui.widget.WidgetContainer;
import leviathan.gui.workspace.GuiWorkspace;
import leviathan.utils.Drawable;

public class EventHandler {

	@SubscribeEvent
	public void onGui(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof GuiMainMenu) {
			event.getButtonList().add(new GuiButton(10, event.getGui().width / 2 + 102, event.getGui().height / 2 + 32, 20, 20, "G"));
		}
	}

	@SubscribeEvent
	public void onButton(GuiScreenEvent.ActionPerformedEvent event) {
		if (event.getGui() instanceof GuiMainMenu) {
			GuiButton button = event.getButton();
			if (button.id == 10) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiWorkspace());
			}
		}
	}

	@SubscribeEvent
	public void onRegisterElements(RegistryEvent.Register<IWidgetType> event) {
		IForgeRegistry<IWidgetType> registry = event.getRegistry();
		//elements
		registry.register(new WidgetType("label", WidgetTab.OBJECTS, () -> new LabelWidget("Text", WidgetAlignment.TOP_LEFT, GuiConstants.DEFAULT_STYLE)));
		registry.register(new WidgetType("text_edit", WidgetTab.OBJECTS, () -> new TextEditWidget(0, 0, 16, 16)));
		registry.register(new WidgetType("texture", WidgetTab.OBJECTS, () -> new TextureWidget(0, 0, 16, 16, Drawable.MISSING)));
		registry.register(new WidgetType("color", WidgetTab.OBJECTS, () -> new ColoredWidget(0, 0, 16, 16, 0xFFFFFFFF)));
		registry.register(new WidgetType("item", WidgetTab.OBJECTS, () -> new ItemWidget(0, 0, ItemStack.EMPTY)));
		registry.register(WidgetType.BACKGROUND);
		//groups
		registry.register(new WidgetType("group", WidgetTab.GROUPS, () -> new WidgetContainer(0, 0, 16, 16), true));
		//registry.register();
	}
}
