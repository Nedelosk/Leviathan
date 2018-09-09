package leviathan.utils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import leviathan.api.gui.style.ITextStyle;
import leviathan.api.gui.tooltip.ITooltipProvider;
import leviathan.api.gui.tooltip.Tooltip;
import leviathan.gui.IGuiSizable;

public class GuiUtil {

	private GuiUtil() {
	}

	@SideOnly(Side.CLIENT)
	public static List<String> getInformation(ItemStack stack) {
		Minecraft minecraft = Minecraft.getMinecraft();
		boolean advancedTooltips = minecraft.gameSettings.advancedItemTooltips;
		return getInformation(stack, minecraft.player, advancedTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
	}

	@SideOnly(Side.CLIENT)
	public static List<String> getInformation(ItemStack stack, EntityPlayer player, ITooltipFlag flag) {
		if (stack.isEmpty()) {
			return Collections.emptyList();
		}
		List<String> tooltip = stack.getTooltip(player, flag);
		for (int i = 0; i < tooltip.size(); ++i) {
			if (i == 0) {
				tooltip.set(i, stack.getRarity().rarityColor + tooltip.get(i));
			} else {
				tooltip.set(i, TextFormatting.GRAY + tooltip.get(i));
			}
		}
		return tooltip;
	}

	public static String getFormattedString(ITextStyle style, String rawText) {
		StringBuilder modifiers = new StringBuilder();
		if (style.isBold()) {
			modifiers.append(TextFormatting.BOLD);
		}
		if (style.isItalic()) {
			modifiers.append(TextFormatting.ITALIC);
		}
		if (style.isUnderlined()) {
			modifiers.append(TextFormatting.UNDERLINE);
		}
		if (style.isStrikethrough()) {
			modifiers.append(TextFormatting.STRIKETHROUGH);
		}
		if (style.isObfuscated()) {
			modifiers.append(TextFormatting.OBFUSCATED);
		}
		modifiers.append(rawText);
		return modifiers.toString();
	}

	/*public static void drawItemStack(GuiForestry gui, ItemStack stack, int xPos, int yPos) {
		drawItemStack(gui.getFontRenderer(), stack, xPos, yPos);
	}*/

	public static void drawItemStack(ItemStack stack, int xPos, int yPos) {
		drawItemStack(Minecraft.getMinecraft().fontRenderer, stack, xPos, yPos);
	}

	public static void drawItemStack(FontRenderer fontRenderer, ItemStack stack, int xPos, int yPos) {
		FontRenderer font = null;
		if (!stack.isEmpty()) {
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null) {
			font = fontRenderer;
		}

		RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
		itemRender.renderItemAndEffectIntoGUI(stack, xPos, yPos);
		itemRender.renderItemOverlayIntoGUI(font, stack, xPos, yPos, null);
	}

	public static void drawTooltips(IGuiSizable gui, @Nullable ITooltipProvider provider, Tooltip toolTips, int mouseX, int mouseY) {
		List<String> lines = toolTips.getLines();
		if (!lines.isEmpty()) {
			GlStateManager.pushMatrix();
			if (provider == null || provider.isRelativeToGui()) {
				GlStateManager.translate(-gui.getGuiLeft(), -gui.getGuiTop(), 0);
			}
			ScaledResolution scaledresolution = new ScaledResolution(gui.getMC());
			GuiUtils.drawHoveringText(lines, mouseX, mouseY, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), -1, gui.getMC().fontRenderer);
			GlStateManager.popMatrix();
		}
	}

	public static void drawTooltips(IGuiSizable gui, Collection<?> objects, int mouseX, int mouseY) {
		for (Object obj : objects) {
			if (!(obj instanceof ITooltipProvider)) {
				continue;
			}
			ITooltipProvider provider = (ITooltipProvider) obj;
			if (!provider.isToolTipVisible()) {
				continue;
			}
			int mX = mouseX;
			int mY = mouseY;
			if (provider.isRelativeToGui()) {
				mX -= gui.getGuiLeft();
				mY -= gui.getGuiTop();
			}
			Tooltip tips = provider.getToolTip(mX, mY);
			if (tips == null) {
				continue;
			}
			boolean mouseOver = provider.isMouseOver(mX, mY);
			tips.onTick(mouseOver);
			if (mouseOver && tips.isReady()) {
				tips.refresh();
				drawTooltips(gui, provider, tips, mouseX, mouseY);
			}
		}
	}
}
