package leviathan.gui.properties;

import java.awt.Color;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import net.minecraftforge.fml.common.registry.ForgeRegistries;

import leviathan.api.geometry.Region;
import leviathan.api.gui.WidgetAlignment;
import leviathan.api.properties.serializer.IPropertySerializer;
import leviathan.api.properties.serializer.PropertySerializer;
import leviathan.api.render.DrawMode;
import leviathan.api.render.IDrawable;
import leviathan.api.render.ISprite;
import leviathan.utils.Drawable;
import leviathan.utils.Sprite;

public class JEGSerializers {
	public static final IntegerSerializer INTEGER = new IntegerSerializer();
	public static final FloatSerializer FLOAT = new FloatSerializer();
	public static final BooleanSerializer BOOLEAN = new BooleanSerializer();
	public static final WidgetSerializer WIDGET = new WidgetSerializer();
	public static final StringSerializer STRING = new StringSerializer();
	public static final EnumSerializer<DrawMode> DRAW_MODE = new EnumSerializer<>(DrawMode.class);
	public static final EnumSerializer<WidgetAlignment> ALIGNMENT = new EnumSerializer<>(WidgetAlignment.class);

	public static final IPropertySerializer<Region> REGION = new PropertySerializer<>(Region.class, creator ->
		creator.startType(0, INTEGER)
			.create("x", Region::getX)
			.create("y", Region::getY)
			.create("width", Region::getWidth)
			.create("height", Region::getHeight),
		property -> new Region(property.getChildValue(Integer.class, "x"), property.getChildValue(Integer.class, "y"), property.getChildValue(Integer.class, "width"), property.getChildValue(Integer.class, "height")));

	public static final IPropertySerializer<ISprite> SPRITE = new PropertySerializer<>(ISprite.class, creator ->
		creator.startType(0, INTEGER)
			.create("u", ISprite::getU)
			.create("v", ISprite::getV)
			.endType()
			.startType(16, INTEGER)
			.create("width", ISprite::getWidth)
			.create("height", ISprite::getHeight)
			.endType()
			.startType(255, INTEGER)
			.create("textureWidth", ISprite::getTextureWidth)
			.create("textureHeight", ISprite::getTextureHeight)
			.endType()
			.add("location", "minecraft:default", sprite -> sprite.getLocation().toString(), STRING),
		property -> {
			String location = property.getChildValue(String.class, "location");
			int u = property.getChildValue(Integer.class, "u");
			int v = property.getChildValue(Integer.class, "v");
			int width = property.getChildValue(Integer.class, "width");
			int height = property.getChildValue(Integer.class, "height");
			int textureWidth = property.getChildValue(Integer.class, "textureWidth");
			int textureHeight = property.getChildValue(Integer.class, "textureHeight");
			return new Sprite(new ResourceLocation(location), u, v, width, height, textureWidth, textureHeight);
		});

	public static final IPropertySerializer<IDrawable> DRAWABLE = new PropertySerializer<>(IDrawable.class, creator ->
		creator.add("mode", DrawMode.SIMPLE, IDrawable::getMode, DRAW_MODE).add("sprite", Drawable.MISSING.getSprite(), IDrawable::getSprite, SPRITE),
		manager -> new Drawable(manager.getChildValue(DrawMode.class, "mode"), manager.getChildValue(Sprite.class, "sprite")));

	public static final IPropertySerializer<Color> COLOR = new PropertySerializer<>(Color.class, creator ->
		creator.startType(0, integer -> MathHelper.clamp(integer, 0, 255), INTEGER)
			.create("red", Color::getRed).create("blue", Color::getBlue).create("green", Color::getGreen),
		property -> new Color(property.getChildValue(Integer.class, "red"), property.getChildValue(Integer.class, "blue"), property.getChildValue(Integer.class, "green")));

	public static final IPropertySerializer<ItemStack> ITEM_STACK = new PropertySerializer<>(ItemStack.class, creator ->
		creator.add("item_name", "", stack -> stack.getItem().getRegistryName().toString(), STRING)
			.add("metadata", 0, ItemStack::getMetadata, INTEGER)
			.add("count", 0, ItemStack::getCount, INTEGER),
		property -> {
			String itemName = property.getChildValue(String.class, "item_name");
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
			return new ItemStack(item == null ? Items.AIR : item, property.getChildValue(Integer.class, "count"), property.getChildValue(Integer.class, "metadata"));
		});
}
